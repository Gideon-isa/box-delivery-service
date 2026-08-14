package com.polarisdigitech.boxdeliveryservice.application.box;

import com.polarisdigitech.boxdeliveryservice.application.box.port.in.GetBatteryLevelUseCase;
import com.polarisdigitech.boxdeliveryservice.domain.box.Battery;
import com.polarisdigitech.boxdeliveryservice.domain.box.Box;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.domain.shared.DomainError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.NotFoundError;
import com.polarisdigitech.boxdeliveryservice.domain.shared.Result;
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
