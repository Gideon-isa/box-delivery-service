package com.polarisdigitech.boxdeliveryservice.delivery.services;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.delivery.domain.DeliveryRepository;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveryView;
import com.polarisdigitech.boxdeliveryservice.delivery.usecases.GetDeliveryUseCase;
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
public class GetDeliveryService implements GetDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    @Override
    public Result<DeliveryView, DomainError> execute(UUID id) {

        Optional<Delivery> deliveryOptional = deliveryRepository.findById(id);
        return deliveryOptional
                .<Result<DeliveryView, DomainError>>map(delivery -> Result.success(DeliveryView.from(delivery)))
                .orElseGet(() -> Result.failure(NotFoundError.of("Delivery", id.toString())));

    }
}
