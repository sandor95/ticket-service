package hu.otp.ticket.service.ticket.api.model;

import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "RESERVATION")
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends DatabaseEntry {

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @Column(name = "SEAT_CODE", nullable = false)
    private String seatCode;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TRANSACTION_ID", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "STATUS", nullable = false)
    private String status;
}