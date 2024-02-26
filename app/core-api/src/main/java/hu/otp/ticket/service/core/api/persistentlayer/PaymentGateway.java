package hu.otp.ticket.service.core.api.persistentlayer;

import hu.otp.ticket.service.core.api.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class PaymentGateway {

    private final PaymentRepository paymentRepository;

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
}