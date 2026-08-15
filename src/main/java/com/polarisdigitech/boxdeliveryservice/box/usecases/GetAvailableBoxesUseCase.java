package com.polarisdigitech.boxdeliveryservice.box.usecases;

import com.polarisdigitech.boxdeliveryservice.box.dto.BoxView;

import java.util.List;

public interface GetAvailableBoxesUseCase {
    List<BoxView> execute();
}
