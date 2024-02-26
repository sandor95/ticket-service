package hu.otp.ticket.service.core.api.persistentlayer;

import java.util.Optional;

import hu.otp.ticket.service.core.api.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class UserGateway {

    private final UserRepository repository;

    public Optional<User> getUserById(@NotNull Long userId) {
        return repository.findById(userId);
    }

    public Optional<User> getUserWithBankCardByCardId(Long userId, String cardId) {
        return repository.findByUserIdAndCardIdWithCard(userId, cardId);
    }
}