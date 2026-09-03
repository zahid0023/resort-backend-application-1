package com.example.resortbackendapplication1.auth.service;

import com.example.resortbackendapplication1.auth.model.entity.PasswordResetOtpEntity;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;

public interface PasswordResetService {

    /**
     * Generates and persists a fresh OTP for {@code userEntity}. Does not deliver it — the caller (see
     * {@code PasswordResetController}) sends it through whichever channel the username resolves to.
     */
    PasswordResetOtpEntity createOtp(UserEntity userEntity);

    /**
     * Validates {@code otpCode} against {@code userEntity}'s most recent unused OTP and marks it used.
     *
     * @throws IllegalArgumentException if no matching unused OTP exists or it has expired
     */
    PasswordResetOtpEntity consumeOtp(UserEntity userEntity, String otpCode);
}
