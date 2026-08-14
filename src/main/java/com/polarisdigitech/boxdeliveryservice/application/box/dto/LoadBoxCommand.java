package com.polarisdigitech.boxdeliveryservice.application.box.dto;

import java.util.List;
import java.util.UUID;

public record LoadBoxCommand(UUID boxId, List<UUID> itemIds) {
}
