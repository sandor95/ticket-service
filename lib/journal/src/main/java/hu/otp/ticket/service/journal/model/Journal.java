package hu.otp.ticket.service.journal.model;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Journal {

    private String user;

    private String application;

    private ZonedDateTime timestamp;

    private JournalType type;

    private String content;
}