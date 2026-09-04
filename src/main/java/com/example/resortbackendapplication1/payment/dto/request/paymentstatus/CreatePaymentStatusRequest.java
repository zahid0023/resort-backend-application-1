package com.example.resortbackendapplication1.payment.dto.request.paymentstatus;

import com.example.resortbackendapplication1.payment.dto.request.paymentstatus.locale.PaymentStatusLocaleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePaymentStatusRequest extends PaymentStatusRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @Valid
    @NotNull
    private PaymentStatusLocaleRequest locale;

}
