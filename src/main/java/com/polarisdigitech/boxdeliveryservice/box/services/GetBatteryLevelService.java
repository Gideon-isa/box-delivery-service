package com.polarisdigitech.boxdeliveryservice.box.services;

import com.polarisdigitech.boxdeliveryservice.box.domain.Battery;
import com.polarisdigitech.boxdeliveryservice.box.domain.Box;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.usecases.GetBatteryLevelUseCase;
import com.polarisdigitech.boxdeliveryservice.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.shared.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBatteryLevelService implements GetBatteryLevelUseCase {

    private final BoxRepository boxRepository;

    @Override
    @Transactional(readOnly = true)
    public Result<String, DomainError> execute(UUID boxId) {
        Optional<Box> boxOptional = boxRepository.findById(BoxId.of(boxId));
        if (boxOptional.isEmpty()) {
            return Result.failure(new NotFoundError("box", boxId.toString()));
        }

        Box box = boxOptional.get();
        Battery battery = box.getBatteryLevel();
        double batteryPercentage = battery.getPercentage();

        return Result.success(batteryPercentage + " %");
    }
}
