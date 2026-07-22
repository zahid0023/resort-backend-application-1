package com.example.resortbackendapplication1.resortroomcategoryprice.model.enums;

public enum PriceTypeCode {

    BAS(0),
    WKD(10),
    WKE(20),
    HOL(100),
    SPE(200);

    private final int defaultPriority;

    PriceTypeCode(int defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public int getDefaultPriority() {
        return defaultPriority;
    }

    public boolean hasFixedPriority() {
        return this == BAS || this == WKD || this == WKE;
    }

    public boolean requiresDays() {
        return this == WKD || this == WKE;
    }

    public boolean requiresDateRange() {
        return this == HOL || this == SPE;
    }

    public static PriceTypeCode fromCode(String code) {
        try {
            return PriceTypeCode.valueOf(code);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown price type code: " + code);
        }
    }
}
