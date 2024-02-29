package hu.otp.ticket.service.ticket.api.model;

import java.time.ZonedDateTime;

import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RESERVATION_LOCK")
public class ReservationLock extends DatabaseEntry {

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TRANSACTION_ID", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdAt;

}