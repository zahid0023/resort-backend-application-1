package com.example.resortbackendapplication1.commons.imagehosting;

import com.example.resortbackendapplication1.commons.dto.response.ImageUploadResponse;
import com.example.resortbackendapplication1.commons.enums.ImageHostingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class S3HostingStrategy implements ImageHostingStrategy {

    @Override
    public ImageHostingProvider provider() {
        return ImageHostingProvider.S3;
    }

    @Override
    public ImageUploadResponse upload(MultipartFile file, Map<String, String> config) {
        // ✅ Validate required keys (reuse your enum)
        provider().validate(config);

        String bucket = config.get("bucket");
        String region = config.get("region");
        String accessKey = config.get("access_key");
        String secretKey = config.get("secret_key");
        try {
            String fileName;
            try (S3Client s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(accessKey, secretKey)
                            )
                    )
                    .build()) {

                fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

                PutObjectRequest putRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(
                        putRequest,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                );
            }

            String url = String.format(
                    "https://%s.s3.%s.amazonaws.com/%s",
                    bucket, region, fileName
            );

            return ImageUploadResponse.builder()
                    .imageUrl(url)
                    .publicId(fileName)
                    .build();
        } catch (IOException ex) {
            log.error("S3 upload failed for '{}': {}", file.getOriginalFilename(), ex.getMessage());
            throw new IllegalStateException("S3 upload failed: " + ex.getMessage(), ex);
        }

    }
}
