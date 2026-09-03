package com.example.resortbackendapplication1.commons.mail;

import lombok.Getter;

/**
 * Resolves which platform-level {@code MailProviderConfig} a system flow should send through — e.g. the POS
 * booking flow looks up {@link #CREATE_USER_EMAIL_NOTIFICATIONS} to email a newly-registered customer their
 * credentials. Not every {@code MailProviderConfig} carries one of these; it's only set on the config
 * designated to back a given system flow.
 */
@Getter
public enum MailProviderConfigCode {
    CREATE_USER_EMAIL_NOTIFICATIONS("Create User Email Notifications"),
    PASSWORD_RESET_EMAIL_NOTIFICATIONS("Password Reset Email Notifications");

    private final String label;

    MailProviderConfigCode(String label) {
        this.label = label;
    }

}
