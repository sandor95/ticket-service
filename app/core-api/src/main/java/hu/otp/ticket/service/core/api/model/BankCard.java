package hu.otp.ticket.service.core.api.model;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import hu.otp.ticket.service.model.Currency;
import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BANK_CARD")
public class BankCard extends DatabaseEntry {

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "CARD_ID", length = 10, nullable = false, unique = true)
    private String cardId;

    @Column(name = "CARD_NUMBER", length = 200, nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "CVC", nullable = false)
    private Short cvc;

    @Column(name = "AMOUNT", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;

    @Column(name = "VALID_TO", columnDefinition = "TIMESTAMP")
    private ZonedDateTime validTo;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();
}