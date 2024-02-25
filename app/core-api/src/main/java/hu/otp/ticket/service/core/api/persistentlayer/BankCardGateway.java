package hu.otp.ticket.service.core.api.persistentlayer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class BankCardGateway {

    private final BankCardRepository repository;
}