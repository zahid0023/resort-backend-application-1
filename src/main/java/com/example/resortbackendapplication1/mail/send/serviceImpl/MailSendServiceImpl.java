package com.example.resortbackendapplication1.mail.send.serviceImpl;

import com.example.resortbackendapplication1.commons.mail.MailConfigSource;
import com.example.resortbackendapplication1.mail.send.service.MailSendService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

/**
 * SMTP is one protocol, unlike image hosting's per-provider SDKs — a single JavaMailSenderImpl, built fresh
 * from whichever resort's config map is passed in, sends through Gmail/Replit/any custom SMTP host alike.
 * There is deliberately no per-provider strategy/registry here.
 */
@Service
@Slf4j
public class MailSendServiceImpl implements MailSendService {

    /** Ports that always speak STARTTLS-upgraded SMTP — {@code useTls=false} on one of these can't ever work. */
    private static final int STARTTLS_PORT = 587;

    @Override
    public void send(MailConfigSource configSource, String to, String subject, String body) {
        Map<String, Object> config = configSource.getConfig();
        requireNonBlank(config, "host", "port", "username", "password", "fromEmail");

        int port = intValue(config, "port");
        boolean useTls = booleanValue(config, "useTls", true);
        if (port == STARTTLS_PORT && !useTls) {
            throw new IllegalArgumentException(
                    "Mail config invalid: port 587 requires useTls=true (STARTTLS) — most SMTP relays, "
                            + "including Gmail, reject authentication without it.");
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(stringValue(config, "host"));
        mailSender.setPort(port);
        mailSender.setUsername(stringValue(config, "username"));
        mailSender.setPassword(stringValue(config, "password"));

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", String.valueOf(useTls));
        properties.put("mail.smtp.starttls.required", String.valueOf(useTls));

        String fromEmail = stringValue(config, "fromEmail");
        String fromName = stringValueOrDefault(config, "fromName", "");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if (fromName.isBlank()) {
                helper.setFrom(fromEmail);
            } else {
                helper.setFrom(fromEmail, fromName);
            }
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Mail sent via provider {} to {}", configSource.getProviderCode(), to);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            log.error("Mail send failed via provider {} to {}: {}", configSource.getProviderCode(), to, ex.getMessage());
            throw new IllegalStateException("Mail send failed: " + ex.getMessage(), ex);
        }
    }

    private void requireNonBlank(Map<String, Object> config, String... keys) {
        for (String key : keys) {
            Object value = config == null ? null : config.get(key);
            if (value == null || value.toString().isBlank()) {
                throw new IllegalArgumentException("Missing required mail config key: " + key);
            }
        }
    }

    private String stringValue(Map<String, Object> config, String key) {
        return config.get(key).toString();
    }

    private String stringValueOrDefault(Map<String, Object> config, String key, String defaultValue) {
        Object value = config.get(key);
        return value == null || value.toString().isBlank() ? defaultValue : value.toString();
    }

    private int intValue(Map<String, Object> config, String key) {
        Object value = config.get(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }

    private boolean booleanValue(Map<String, Object> config, String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(value.toString());
    }
}
