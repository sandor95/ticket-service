package hu.otp.ticket.service.journal.model;

import java.time.ZonedDateTime;

import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "JOURNAL")
@NoArgsConstructor
@AllArgsConstructor
public class JournalDbModel extends DatabaseEntry {

    @Column(name = "USER")
    private String user;

    @Column(name = "APPLICATION")
    private String application;

    @Column(name = "TIMESTAMP")
    private ZonedDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private JournalType type;

    @Lob
    @Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT")
    private String content;
}