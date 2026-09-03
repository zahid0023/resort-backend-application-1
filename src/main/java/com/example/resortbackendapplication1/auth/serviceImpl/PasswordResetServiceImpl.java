package com.example.resortbackendapplication1.auth.serviceImpl;

import com.example.resortbackendapplication1.auth.model.entity.PasswordResetOtpEntity;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.repository.PasswordResetOtpRepository;
import com.example.resortbackendapplication1.auth.service.PasswordResetService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom OTP_RANDOM = new SecureRandom();

    private final PasswordResetOtpRepository passwordResetOtpRepository;

    @Value("${jwt.otp.expiration-minutes}")
    private Integer otpExpirationMinutes;

    public PasswordResetServiceImpl(PasswordResetOtpRepository passwordResetOtpRepository) {
        this.passwordResetOtpRepository = passwordResetOtpRepository;
    }

    @Override
    @Transactional
    public PasswordResetOtpEntity createOtp(UserEntity userEntity) {
        PasswordResetOtpEntity otpEntity = new PasswordResetOtpEntity();
        otpEntity.setUserEntity(userEntity);
        otpEntity.setOtpCode(generateOtpCode());
        otpEntity.setExpiresAt(Instant.now().plus(Duration.ofMinutes(otpExpirationMinutes)));
        return passwordResetOtpRepository.save(otpEntity);
    }

    @Override
    @Transactional
    public PasswordResetOtpEntity consumeOtp(UserEntity userEntity, String otpCode) {
        PasswordResetOtpEntity otpEntity = passwordResetOtpRepository
                .findFirstByUserEntity_IdAndOtpCodeAndUsedAtIsNullAndIsActiveAndIsDeletedOrderByCreatedAtDesc(
                        userEntity.getId(), otpCode, true, false)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset code."));

        if (otpEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Invalid or expired reset code.");
        }

        otpEntity.setUsedAt(Instant.now());
        return passwordResetOtpRepository.save(otpEntity);
    }

    private String generateOtpCode() {
        int bound = (int) Math.pow(10, OTP_LENGTH);
        int code = OTP_RANDOM.nextInt(bound);
        return String.format("%0" + OTP_LENGTH + "d", code);
    }
}
