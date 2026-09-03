package com.example.resortbackendapplication1.whatsapp.send.serviceImpl;

import com.example.resortbackendapplication1.whatsapp.send.service.WhatsAppSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Stub — no WhatsApp provider (Twilio, Meta Cloud API, etc.) is wired up yet, so this logs the message instead
 * of actually sending it. Callers (the POS booking flow's welcome message, the forgot-password OTP flow) are
 * written against {@link WhatsAppSendService} so swapping in a real provider later is a serviceImpl-only change.
 */
@Service
@Slf4j
public class WhatsAppSendServiceImpl implements WhatsAppSendService {

    @Override
    public void send(String to, String message) {
        log.info("[WhatsApp stub] To: {} | Message: {}", to, message);
    }
}
