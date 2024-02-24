package hu.otp.partner.persistentlayer;

import java.util.Optional;

import hu.otp.partner.common.model.Event;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "store.type", havingValue = "mysql", matchIfMissing = true)
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
        select e from Event e
        join fetch e.tickets t
        join fetch t.seat s
        where e.id = :id
        """)
    Optional<Event> findByIdWithDetails(Long id);

}