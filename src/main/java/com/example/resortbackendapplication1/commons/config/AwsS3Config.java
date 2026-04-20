package com.example.resortbackendapplication1.commons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.example.resortbackendapplication1.commons.imagehosting.ImageHostingConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@ConfigurationProperties(prefix = "aws.s3")
@Getter
@Setter
@Slf4j
public class AwsS3Config implements ImageHostingConfig {

    private String bucket;
    private String region;
    private String accessKey;
    private String secretKey;

    public S3Client init(String bucket, String region, String accessKey, String secretKey) {
        validate(bucket, region, accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    private void validate(String bucket, String region, String accessKey, String secretKey) {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalStateException("AWS S3 bucket is not configured");
        }
        if (region == null || region.isBlank()) {
            throw new IllegalStateException("AWS S3 region is not configured");
        }
        if (accessKey == null || accessKey.isBlank()) {
            throw new IllegalStateException("AWS S3 access-key is not configured");
        }
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("AWS S3 secret-key is not configured");
        }
    }
}
