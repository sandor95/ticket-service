package hu.otp.ticket.service.core.api.persistentlayer;

import java.util.Optional;

import hu.otp.ticket.service.core.api.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {

    Optional<BankCard> findByCardId(String cardId);
}