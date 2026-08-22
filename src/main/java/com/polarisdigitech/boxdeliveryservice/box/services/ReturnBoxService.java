package com.polarisdigitech.boxdeliveryservice.box.services;

import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxState;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.ReturnBoxResponse;
import com.polarisdigitech.boxdeliveryservice.box.usecases.ReturnBoxUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.BusinessRuleViolation;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReturnBoxService implements ReturnBoxUseCase {

    private final BoxRepository boxRepository;
    private final CurrentUser currentUser;

    @Transactional
    @Override
    public Result<ReturnBoxResponse, DomainError> execute(UUID id) {
        BoxId boxId = BoxId.of(id);
        Optional<Box> boxOptional = boxRepository.findById(boxId);
        if (boxOptional.isEmpty()) {
            return Result.failure(NotFoundError.of("Delivery", boxId.toString()));
        }
        Box box = boxOptional.get();
        BoxState state = box.getState();

        Result<Box, DomainError> transitionBox = box.transitionTo(BoxState.RETURNING);
        if (transitionBox.isFailure()) {
            Result.failure(transitionBox.getError());
        }

        box.markModified(currentUser.getId());
        boxRepository.save(box);

        return Result.success(ReturnBoxResponse
                .to("Box is returning to base. State has moved to %s".formatted(BoxState.RETURNING.toString())));
    }
}
