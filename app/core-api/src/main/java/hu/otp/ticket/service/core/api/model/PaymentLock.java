package hu.otp.ticket.service.core.api.model;

import java.time.ZonedDateTime;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENT_LOCK")
public class PaymentLock extends DatabaseEntry {

    @Column(name = "TRANSACTION_ID", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "CARD_ID", nullable = false, length = 10)
    private String cardId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdAt;

}