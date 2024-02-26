package hu.otp.ticket.service.core.api.persistentlayer;

import java.util.Optional;

import hu.otp.ticket.service.core.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        select user from User user
        join fetch user.cards card
        where user.id = :userId
        and card.cardId = :cardId
        """)
    Optional<User> findByUserIdAndCardIdWithCard(Long userId, String cardId);
}