package hu.otp.ticket.service.core.api.persistentlayer;

import java.util.Optional;

import hu.otp.ticket.service.core.api.model.BankCard;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class BankCardGateway {

    private final BankCardRepository repository;

    public Optional<BankCard> getByCardId(@NotNull String cardId) {
        return repository.findByCardId(cardId);
    }

    public BankCard save(BankCard bankCard) {
        return repository.save(bankCard);
    }
}