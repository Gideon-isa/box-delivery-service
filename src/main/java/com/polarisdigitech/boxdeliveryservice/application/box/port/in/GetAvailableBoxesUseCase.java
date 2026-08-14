package com.polarisdigitech.boxdeliveryservice.application.box.port.in;

import com.polarisdigitech.boxdeliveryservice.application.box.dto.BoxView;

import java.util.List;

public interface GetAvailableBoxesUseCase {
    List<BoxView> execute();
}
