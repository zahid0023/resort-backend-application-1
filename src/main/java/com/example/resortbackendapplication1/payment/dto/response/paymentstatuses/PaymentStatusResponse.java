package com.example.resortbackendapplication1.payment.dto.response.paymentstatuses;

import com.example.resortbackendapplication1.payment.model.dto.PaymentStatusDto;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentStatusResponse {
    private final PaymentStatusDto data;

    public PaymentStatusResponse(PaymentStatusDto data) {
        this.data = data;
    }
}
