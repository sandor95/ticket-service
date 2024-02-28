package hu.otp.ticket.service.coreapi.client;

import hu.otp.ticket.service.core.api.client.ApiClient;
import hu.otp.ticket.service.core.api.client.api.PaymentControllerApi;
import hu.otp.ticket.service.core.api.client.api.TokenValidationControllerApi;
import hu.otp.ticket.service.core.api.client.model.PaymentRequestDTO;
import hu.otp.ticket.service.core.api.client.model.PaymentResponseDTO;
import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Component
public class CoreApiClient {

    private final TokenValidationControllerApi tokenValidationApi;

    private final PaymentControllerApi paymentControllerApi;

    @Autowired
    public CoreApiClient(ApiClient apiClient) {
        this.tokenValidationApi = new TokenValidationControllerApi(apiClient);
        this.paymentControllerApi = new PaymentControllerApi(apiClient);
    }

    public TokenValidationResultDTO validateToken(Long userId, String token) {
        return tokenValidationApi.validate(userId, token);
    }

    public PaymentResponseDTO pay(PaymentRequestDTO paymentRequestDTO, String token) {
        return paymentControllerApi.pay(paymentRequestDTO, token);
    }
}