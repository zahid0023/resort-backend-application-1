package com.example.resortbackendapplication1.payment.dto.response.paymentmethods;

import com.example.resortbackendapplication1.payment.model.dto.PaymentMethodDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentMethodResponse {
    private final PaymentMethodDto data;

    public PaymentMethodResponse(PaymentMethodDto data) {
        this.data = data;
    }
}
