package com.example.resortbackendapplication1.image.upload.service;

import com.example.resortbackendapplication1.commons.imagehosting.ImageHostingConfigSource;
import com.example.resortbackendapplication1.image.upload.dto.response.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {

    ImageUploadResponse upload(MultipartFile file, ImageHostingConfigSource configSource);

    List<ImageUploadResponse> uploadAll(List<MultipartFile> files, ImageHostingConfigSource configSource);

    void delete(String publicId, ImageHostingConfigSource configSource);
}
