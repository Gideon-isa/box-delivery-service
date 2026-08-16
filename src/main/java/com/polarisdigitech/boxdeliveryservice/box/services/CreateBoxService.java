package com.polarisdigitech.boxdeliveryservice.box.services;

import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.domain.TxRef;
import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.dto.CreateBoxCommand;
import com.polarisdigitech.boxdeliveryservice.box.usecases.CreateBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.shared.BusinessRuleViolation;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
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

        //UUID userId = currentUser.getId();
        UUID userId = UUID.randomUUID();

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
