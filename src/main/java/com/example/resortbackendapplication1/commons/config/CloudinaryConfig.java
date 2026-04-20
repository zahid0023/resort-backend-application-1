package com.example.resortbackendapplication1.commons.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.example.resortbackendapplication1.commons.imagehosting.ImageHostingConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cloudinary")
@Getter
@Setter
@Slf4j
public class CloudinaryConfig implements ImageHostingConfig {

    private String cloudName;
    private String apiKey;
    private String apiSecret;

    public Cloudinary init(String cloudName, String apiKey, String apiSecret) {
        validate(cloudName, apiKey, apiSecret);
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    private void validate(String cloudName, String apiKey, String apiSecret) {
        if (cloudName == null || cloudName.isBlank()) {
            throw new IllegalStateException("Cloudinary cloud-name is not configured");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Cloudinary api-key is not configured");
        }
        if (apiSecret == null || apiSecret.isBlank()) {
            throw new IllegalStateException("Cloudinary api-secret is not configured");
        }
    }
}
