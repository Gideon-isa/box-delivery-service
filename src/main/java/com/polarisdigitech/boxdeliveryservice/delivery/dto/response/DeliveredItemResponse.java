package com.polarisdigitech.boxdeliveryservice.delivery.dto.response;

import com.polarisdigitech.boxdeliveryservice.item.domain.Item;
import com.polarisdigitech.boxdeliveryservice.item.dto.ItemView;

import java.util.List;

public record DeliveredItemResponse(List<ItemView> deliveredItems, String response,  boolean isReturning) {

    public static DeliveredItemResponse from(List<Item> deliveredItems) {

        List<ItemView> itemViews = deliveredItems.stream().map(ItemView::from).toList();
        return new DeliveredItemResponse(itemViews, "Items delivered; returning",  true);
    }
}
