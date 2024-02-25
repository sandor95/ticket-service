package hu.otp.partner.query.model;

import hu.otp.ticket.service.model.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder
public record SeatDTO(String id,
                      Integer price,
                      @Schema(implementation = String.class)
                      Currency currency,
                      boolean reserved) {
}