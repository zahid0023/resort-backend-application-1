package com.example.resortbackendapplication1.payment.dto.request.paymentmethod;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentMethodRequest {

    @NotNull
    private Integer sortOrder;

}
