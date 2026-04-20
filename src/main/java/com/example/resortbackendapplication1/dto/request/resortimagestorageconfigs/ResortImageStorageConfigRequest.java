package com.example.resortbackendapplication1.dto.request.resortimagestorageconfigs;

import com.example.resortbackendapplication1.commons.enums.ImageHostingProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public abstract class ResortImageStorageConfigRequest {

    @NotNull(message = "Provider must not be null")
    private ImageHostingProvider provider;

    @NotNull(message = "Config must not be null")
    private Map<String, String> config;
}
