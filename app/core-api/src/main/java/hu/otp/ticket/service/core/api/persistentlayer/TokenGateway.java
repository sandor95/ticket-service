package hu.otp.ticket.service.core.api.persistentlayer;

import java.util.Optional;

import hu.otp.ticket.service.core.api.model.Token;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class TokenGateway {

    private final TokenRepository repository;

    public Optional<Token> getTokenByTokenValue(@NotNull String tokenValue) {
        return repository.findByValue(tokenValue);
    }
}