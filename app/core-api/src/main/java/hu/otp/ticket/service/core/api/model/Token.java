package hu.otp.ticket.service.core.api.model;

import java.time.ZonedDateTime;

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
@Table(name = "TOKEN")
public class Token extends DatabaseEntry {

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "TOKEN", columnDefinition = "MEDIUMTEXT", nullable = false, unique = true)
    private String value;

    @Column(name = "VALID_TO", columnDefinition = "TIMESTAMP")
    private ZonedDateTime validTo;

}