package com.example.resortbackendapplication1.payment.dto.request.paymentprovider;

import com.example.resortbackendapplication1.payment.dto.request.paymentprovider.locale.PaymentProviderLocaleRequest;
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
public class CreatePaymentProviderRequest extends PaymentProviderRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotNull
    private Long paymentMethodId;

    @Valid
    @NotNull
    private PaymentProviderLocaleRequest locale;

}
