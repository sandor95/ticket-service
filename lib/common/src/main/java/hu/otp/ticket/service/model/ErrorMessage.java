package hu.otp.ticket.service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorMessage {

    private final String message;

    private final String id;
}