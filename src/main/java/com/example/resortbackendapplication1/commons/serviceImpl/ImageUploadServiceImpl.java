package com.example.resortbackendapplication1.commons.serviceImpl;

import com.example.resortbackendapplication1.commons.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.commons.enums.ImageHostingProvider;
import com.example.resortbackendapplication1.commons.imagehosting.ImageHostingStrategyRegistry;
import com.example.resortbackendapplication1.commons.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

    private final ImageHostingStrategyRegistry registry;

    @Override
    public ImageUploadResponse upload(MultipartFile file, ImageHostingProvider provider, Map<String, String> config) {
        return registry.get(provider).upload(file, config);
    }
}
