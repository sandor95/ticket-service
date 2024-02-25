package hu.otp.ticket.service.core.api.persistentlayer;

import hu.otp.ticket.service.core.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}