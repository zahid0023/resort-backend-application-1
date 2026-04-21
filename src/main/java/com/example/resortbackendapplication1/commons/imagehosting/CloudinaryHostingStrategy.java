package com.example.resortbackendapplication1.commons.imagehosting;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.resortbackendapplication1.commons.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.commons.enums.ImageHostingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class CloudinaryHostingStrategy implements ImageHostingStrategy {

    @Override
    public ImageHostingProvider provider() {
        return ImageHostingProvider.CLOUDINARY;
    }

    @Override
    public ImageUploadResponse upload(MultipartFile file, Map<String, String> config) {
        provider().validate(config);

        String cloudName = config.get("cloud_name");
        String apiKey = config.get("api_key");
        String apiSecret = config.get("api_secret");

        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true
            ));

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );

            String url = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            return ImageUploadResponse.builder()
                    .imageUrl(url)
                    .publicId(publicId)
                    .build();
        } catch (IOException ex) {
            log.error("Cloudinary upload failed for '{}': {}", file.getOriginalFilename(), ex.getMessage());
            throw new IllegalStateException("Cloudinary upload failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(String publicId, Map<String, String> config) {
        provider().validate(config);
        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", config.get("cloud_name"),
                    "api_key", config.get("api_key"),
                    "api_secret", config.get("api_secret"),
                    "secure", true
            ));
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException ex) {
            log.error("Cloudinary delete failed for publicId '{}': {}", publicId, ex.getMessage());
            throw new IllegalStateException("Cloudinary delete failed: " + ex.getMessage(), ex);
        }
    }
}
