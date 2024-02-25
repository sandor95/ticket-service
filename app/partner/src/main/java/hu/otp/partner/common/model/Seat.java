package hu.otp.partner.common.model;

import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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
@Table(name = "SEAT")
public class Seat extends DatabaseEntry {

    @Column(name = "SEAT_CODE")
    @Size(max = 10)
    private String seatCode;
}