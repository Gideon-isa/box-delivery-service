package com.polarisdigitech.boxdeliveryservice.application.box;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.application.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.application.box.port.in.CreateBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.domain.box.Box;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.domain.box.TxRef;
import com.polarisdigitech.boxdeliveryservice.domain.shared.BusinessRuleViolation;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateBoxService implements CreateBoxUseCase {
    private final BoxRepository boxRepository;
    private final CurrentUser currentUser;

    @Override
    @Transactional
    public Result<BoxView, DomainError> execute(CreateBoxCommand command) {

        UUID userId = currentUser.getId();

        Result<TxRef, DomainError> txRefResult = TxRef.of(command.txRef());
        if (txRefResult.isFailure()) {
            return Result.failure(txRefResult.getError());
        }

        if (boxRepository.existsByTxRef(txRefResult.getValue())) {
            return Result.failure(BusinessRuleViolation.of(
                    "DUPLICATE_TXREF", "A box with txref '" + command.txRef() + "' already exists"));
        }

        Result<Box, DomainError> boxResult =
                Box.create(command.txRef(), command.weightLimitGrams(), command.batteryPercentage(), userId);
        if (boxResult.isFailure()) {
            return Result.failure(boxResult.getError());
        }

        Box saved = boxRepository.save(boxResult.getValue());
        return Result.success(BoxView.from(saved));
    }
}
