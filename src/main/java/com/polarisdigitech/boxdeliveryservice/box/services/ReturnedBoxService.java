package com.polarisdigitech.boxdeliveryservice.box.services;

import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxState;
import com.polarisdigitech.boxdeliveryservice.box.dto.response.ReturnBoxResponse;
import com.polarisdigitech.boxdeliveryservice.box.usecases.ReturnedBoxUseCase;
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
public class ReturnedBoxService implements ReturnedBoxUseCase {

    private final BoxRepository boxRepository;

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

        Result<Box, DomainError> transitionBox = box.transitionTo(BoxState.IDLE);
        if (transitionBox.isFailure()) {
            Result.failure(transitionBox.getError());
        }
        boxRepository.save(box);
        return Result.success(ReturnBoxResponse.to("Box has returned to base. State moved to IDLE "));
    }
}
