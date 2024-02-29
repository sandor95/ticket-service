package hu.otp.ticket.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ErrorMessage {

    private final String message;

    private final String id;
}