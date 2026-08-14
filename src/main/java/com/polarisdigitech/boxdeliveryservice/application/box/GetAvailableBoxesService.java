package com.polarisdigitech.boxdeliveryservice.application.box;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;
import com.polarisdigitech.boxdeliveryservice.application.box.port.in.GetAvailableBoxesUseCase;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxRepository;
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
        return boxRepository.findAvailableForLoading().stream()
                .map(BoxView::from)
                .toList();
    }
}
