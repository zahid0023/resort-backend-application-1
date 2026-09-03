package com.example.resortbackendapplication1.auth.controller;

import com.example.resortbackendapplication1.auth.dto.request.ForgotPasswordRequest;
import com.example.resortbackendapplication1.auth.dto.request.ResetPasswordRequest;
import com.example.resortbackendapplication1.auth.model.entity.PasswordResetOtpEntity;
import com.example.resortbackendapplication1.auth.model.entity.UserEntity;
import com.example.resortbackendapplication1.auth.service.PasswordResetService;
import com.example.resortbackendapplication1.auth.service.UserService;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.commons.mail.MailProviderConfigCode;
import com.example.resortbackendapplication1.commons.utils.ContactValueUtils;
import com.example.resortbackendapplication1.mail.provider.model.entity.MailProviderConfigEntity;
import com.example.resortbackendapplication1.mail.provider.service.MailProviderConfigService;
import com.example.resortbackendapplication1.mail.send.service.MailSendService;
import com.example.resortbackendapplication1.whatsapp.send.service.WhatsAppSendService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The only way an auto-registered customer's random password (see {@code ResortBookingController#registerCustomer})
 * ever becomes usable: a code is sent to their username's channel (email, or WhatsApp when the matching phone is
 * WhatsApp-reachable) via {@link #forgotPassword}, then redeemed for a real password via {@link #resetPassword}.
 * Both endpoints fall under {@code /api/v1/auth/**}, already public in {@code SecurityConfig}.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final MailProviderConfigService mailProviderConfigService;
    private final MailSendService mailSendService;
    private final WhatsAppSendService whatsAppSendService;

    @Value("${jwt.otp.expiration-minutes}")
    private Integer otpExpirationMinutes;

    public PasswordResetController(UserService userService,
                                   PasswordResetService passwordResetService,
                                   MailProviderConfigService mailProviderConfigService,
                                   MailSendService mailSendService,
                                   WhatsAppSendService whatsAppSendService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
        this.mailProviderConfigService = mailProviderConfigService;
        this.mailSendService = mailSendService;
        this.whatsAppSendService = whatsAppSendService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        UserEntity userEntity = userService.getUserByUsername(request.getUsername());
        PasswordResetOtpEntity otpEntity = passwordResetService.createOtp(userEntity);
        deliverOtp(userEntity, request.getUsername(), otpEntity.getOtpCode());
        return ResponseEntity.ok(new SuccessResponse(true, otpEntity.getId()));
    }

    /**
     * {@code @Transactional} here (rather than left to {@code consumeOtp}/{@code resetPassword} individually)
     * is what makes the two atomic: both join this one transaction, so if the password update fails the OTP's
     * {@code used_at} write rolls back with it instead of burning the code for nothing.
     */
    @Transactional
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match.");
        }

        UserEntity userEntity = userService.getUserByUsername(request.getUsername());
        passwordResetService.consumeOtp(userEntity, request.getOtp());
        SuccessResponse response = userService.resetPassword(userEntity, request.getNewPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * Not best-effort, unlike the booking flow's welcome message — without a channel to deliver the code
     * through, the customer has no way to complete the reset, so a missing mail config or a non-WhatsApp phone
     * fails the request (see {@link com.example.resortbackendapplication1.commons.exception.GlobalExceptionHandler}'s
     * {@code IllegalStateException} handler) rather than silently swallowing it.
     */
    private void deliverOtp(UserEntity userEntity, String username, String otpCode) {
        String message = "Your password reset code is " + otpCode + ". It expires in " + otpExpirationMinutes
                + " minute(s). If you didn't request this, you can ignore this message.";

        if (ContactValueUtils.isEmail(username)) {
            MailProviderConfigEntity configEntity = mailProviderConfigService
                    .getEntityByCode(MailProviderConfigCode.PASSWORD_RESET_EMAIL_NOTIFICATIONS)
                    .orElseThrow(() -> new IllegalStateException(
                            "No mail provider is configured to send password reset emails."));
            mailSendService.send(configEntity, username, "Your Password Reset Code", message);
            return;
        }

        boolean isWhatsappReachable = userEntity.getUserPhoneEntities().stream()
                .anyMatch(phone -> phone.getPhone().equals(username) && Boolean.TRUE.equals(phone.getIsWhatsapp()));
        if (!isWhatsappReachable) {
            throw new IllegalStateException(
                    "This phone number is not reachable on WhatsApp, so a password reset code can't be delivered.");
        }
        whatsAppSendService.send(username, message);
    }
}
