package com.example.resortbackendapplication1.commons.imagehosting;

import com.example.resortbackendapplication1.commons.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.commons.enums.ImageHostingProvider;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageHostingStrategy {
    ImageHostingProvider provider();

    ImageUploadResponse upload(MultipartFile file, Map<String, String> configs);
}
