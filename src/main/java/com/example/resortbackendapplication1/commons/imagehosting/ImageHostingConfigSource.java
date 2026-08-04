package com.example.resortbackendapplication1.commons.imagehosting;

import java.util.Map;

public interface ImageHostingConfigSource {

    String getProviderCode();

    Map<String, Object> getConfig();
}
