package com.example.resortbackendapplication1.mail.send.service;

import com.example.resortbackendapplication1.commons.mail.MailConfigSource;

public interface MailSendService {

    void send(MailConfigSource configSource, String to, String subject, String body);
}
