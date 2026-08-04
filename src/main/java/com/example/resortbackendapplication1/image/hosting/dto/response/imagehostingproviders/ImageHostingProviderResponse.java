package com.example.resortbackendapplication1.image.hosting.dto.response.imagehostingproviders;

import com.example.resortbackendapplication1.image.hosting.model.dto.ImageHostingProviderDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageHostingProviderResponse {
    private final ImageHostingProviderDto data;

    public ImageHostingProviderResponse(ImageHostingProviderDto data) {
        this.data = data;
    }
}
