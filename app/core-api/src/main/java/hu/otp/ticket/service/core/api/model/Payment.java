package hu.otp.ticket.service.core.api.model;

import java.time.ZonedDateTime;

import hu.otp.ticket.service.model.Currency;
import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.*;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENT")
public class Payment extends DatabaseEntry {

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private BankCard card;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "CURRENCY")
    private Currency currency;

    @Column(name = "PAYMENT_TRANSACTION_ID")
    private String paymentTransactionId;

    @Column(name = "TRANSACTION_TIME", columnDefinition = "TIMESTAMP")
    private ZonedDateTime transactionTime;

    @Column(name = "ERROR_CODE")
    private Integer errorCode;
}