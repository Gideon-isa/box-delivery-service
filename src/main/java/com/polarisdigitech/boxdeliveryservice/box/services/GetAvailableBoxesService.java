package com.polarisdigitech.boxdeliveryservice.box.services;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxRepository;
import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.box.usecases.GetAvailableBoxesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAvailableBoxesService implements GetAvailableBoxesUseCase {

    private final BoxRepository boxRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BoxView> execute() {
        return boxRepository.findAvailableForLoading()
                .stream()
                .map(BoxView::from)
                .toList();
    }
}
