package com.example.resortbackendapplication1.dto.request.resorts;

import com.example.resortbackendapplication1.commons.dto.request.ImageMetaRequest;
import com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs.CreateResortImageStorageConfigRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateResortRequest extends ResortRequest {
    private CreateResortImageStorageConfigRequest configRequest;
    private List<ImageMetaRequest> images = new ArrayList<>();
}
