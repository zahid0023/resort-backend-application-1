package com.example.resortbackendapplication1.commons.mail;

import java.util.Map;

public interface MailConfigSource {

    String getProviderCode();

    Map<String, Object> getConfig();
}
