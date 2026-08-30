package com.example.resortbackendapplication1.commons.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class ContactValueUtils {

    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");

    public boolean isEmail(String value) {
        return value != null && EMAIL_PATTERN.matcher(value).matches();
    }

    public boolean isPhone(String value) {
        return value != null && PHONE_PATTERN.matcher(value).matches();
    }
}
