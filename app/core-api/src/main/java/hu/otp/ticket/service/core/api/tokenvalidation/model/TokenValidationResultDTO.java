package hu.otp.ticket.service.core.api.tokenvalidation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TokenValidationResultDTO(@Schema(implementation = String.class)
                                       ValidationResult result,
                                       Integer errorCode) {
}