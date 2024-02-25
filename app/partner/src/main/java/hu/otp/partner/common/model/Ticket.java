package hu.otp.partner.common.model;

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
@Table(name = "TICKET")
public class Ticket extends DatabaseEntry {

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "CURRENCY")
    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;
}