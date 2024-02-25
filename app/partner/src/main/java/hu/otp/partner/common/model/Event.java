package hu.otp.partner.common.model;

import java.time.ZonedDateTime;
import java.util.Set;

import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.*;
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
@Table(name = "EVENT")
public class Event extends DatabaseEntry {

    @Column(name = "TITLE")
    @Size(max = 200)
    private String title;

    @Column(name = "LOCATION")
    @Size(max = 100)
    private String location;

    @Column(name = "START_TIME", columnDefinition = "TIMESTAMP")
    private ZonedDateTime startTime;

    @Column(name = "END_TIME", columnDefinition = "TIMESTAMP")
    private ZonedDateTime endTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets;
}