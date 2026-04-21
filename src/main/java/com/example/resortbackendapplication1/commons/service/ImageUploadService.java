package com.example.resortbackendapplication1.commons.service;

import com.example.resortbackendapplication1.commons.dto.request.ImageMetaRequest;
import com.example.resortbackendapplication1.commons.dto.request.ImageRequest;
import com.example.resortbackendapplication1.commons.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.commons.enums.ImageHostingProvider;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImageUploadService {
    ImageUploadResponse upload(MultipartFile file,
                               ImageHostingProvider provider,
                               Map<String, String> config);

    void delete(String publicId,
                ImageHostingProvider provider,
                Map<String, String> config);

    List<ImageRequest> uploadAll(List<MultipartFile> images,
                                 List<ImageMetaRequest> metaList,
                                 ImageHostingProvider provider,
                                 Map<String, String> config);
}
