package hu.otp.ticket.service.journal;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Journal {

    private String user;

    private String application;

    private ZonedDateTime timestamp;

    private JournalType type;

    private String content;
}