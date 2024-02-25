package hu.otp.partner.knownobject;

import static hu.otp.partner.knownobject.Seats.*;

import hu.otp.partner.common.model.Ticket;
import hu.otp.partner.query.model.SeatDTO;
import hu.otp.ticket.service.model.Currency;

public class Tickets {

    public static final Ticket _30Y_CONCERT_S1 = Ticket.builder()
                                                        .price(4590)
                                                        .currency(Currency.HUF)
                                                        .seat(S1)
                                                        .build();


    public static final Ticket _30Y_CONCERT_S2 = Ticket.builder()
                                                        .price(4590)
                                                        .currency(Currency.HUF)
                                                        .seat(S2)
                                                        .build();

    public static final Ticket _30Y_CONCERT_S3 = Ticket.builder()
                                                        .price(4590)
                                                        .currency(Currency.HUF)
                                                        .seat(S3)
                                                        .reservationId(12L)
                                                        .build();


    public static final Ticket _30Y_CONCERT_S4 = Ticket.builder()
                                                        .price(4590)
                                                        .currency(Currency.HUF)
                                                        .seat(S4)
                                                        .reservationId(1L)
                                                        .build();
    public static final SeatDTO TICKET_S1_DTO = SeatDTO.builder()
                                                        .id(_30Y_CONCERT_S1.getSeat().getSeatCode())
                                                        .price(_30Y_CONCERT_S1.getPrice())
                                                        .currency(_30Y_CONCERT_S1.getCurrency())
                                                        .reserved(_30Y_CONCERT_S1.getReservationId() != null)
                                                        .build();

    public static final SeatDTO TICKET_S2_DTO = SeatDTO.builder()
                                                        .id(_30Y_CONCERT_S2.getSeat().getSeatCode())
                                                        .price(_30Y_CONCERT_S2.getPrice())
                                                        .currency(_30Y_CONCERT_S2.getCurrency())
                                                        .reserved(_30Y_CONCERT_S2.getReservationId() != null)
                                                        .build();

    public static final SeatDTO TICKET_S3_DTO = SeatDTO.builder()
                                                        .id(_30Y_CONCERT_S3.getSeat().getSeatCode())
                                                        .price(_30Y_CONCERT_S3.getPrice())
                                                        .currency(_30Y_CONCERT_S3.getCurrency())
                                                        .reserved(_30Y_CONCERT_S3.getReservationId() != null)
                                                        .build();

    public static final SeatDTO TICKET_S4_DTO = SeatDTO.builder()
                                                        .id(_30Y_CONCERT_S4.getSeat().getSeatCode())
                                                        .price(_30Y_CONCERT_S4.getPrice())
                                                        .currency(_30Y_CONCERT_S4.getCurrency())
                                                        .reserved(_30Y_CONCERT_S4.getReservationId() != null)
                                                        .build();

}