package com.example.resortbackendapplication1.payment.dto.response.paymentproviders;

import com.example.resortbackendapplication1.payment.model.dto.PaymentProviderDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentProviderResponse {
    private final PaymentProviderDto data;

    public PaymentProviderResponse(PaymentProviderDto data) {
        this.data = data;
    }
}
