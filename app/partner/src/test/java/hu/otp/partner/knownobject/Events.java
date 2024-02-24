package hu.otp.partner.knownobject;

import static hu.otp.partner.knownobject.Dates.*;
import static hu.otp.partner.knownobject.Tickets.*;

import java.util.Collections;
import java.util.Set;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.query.model.EventDTO;
import hu.otp.partner.query.model.EventDetailsDTO;

public class Events {

    private static final String _30Y_HETVEGE = "30Y - gigahétvége";
    private static final long ID_123 = 123L;
    private static final String EGER = "Eger";
    private static final String BETON_HOFI_88 = "Beton.Hofi 88";
    private static final long ID_456 = 456L;

    private static final String BUDAPEST = "Budapest";

    public static final Event _30Y_CONCERT = Event.builder()
                                                   .id(ID_123)
                                                   .title(_30Y_HETVEGE)
                                                   .startTime(MAY_25)
                                                   .endTime(MAY_26)
                                                   .location(EGER)
                                                   .tickets(Set.of(_30Y_CONCERT_S1, _30Y_CONCERT_S2, _30Y_CONCERT_S3, _30Y_CONCERT_S4))
                                                   .build();

    public static final EventDTO _30Y_CONCERT_DTO = EventDTO.builder()
                                                            .eventId(ID_123)
                                                            .title(_30Y_HETVEGE)
                                                            .startTimeStamp(MAY_25.toInstant().toEpochMilli())
                                                            .endTimeStamp(MAY_26.toInstant().toEpochMilli())
                                                            .location(EGER)
                                                            .build();

    public static final EventDetailsDTO _30Y_CONCERT_DETAILS_DTO = EventDetailsDTO.builder()
                                                                                .eventId(ID_123)
                                                                                .seats(Set.of(TICKET_S1_DTO, TICKET_S2_DTO, TICKET_S3_DTO, TICKET_S4_DTO))
                                                                                .build();

    public static final Event BETON_HOFI_CONCERT = Event.builder()
                                                   .id(ID_456)
                                                   .title(BETON_HOFI_88)
                                                   .startTime(AUG_8_20H)
                                                   .endTime(AUG_8_22H)
                                                   .location(BUDAPEST)
                                                   .tickets(Collections.emptySet())
                                                   .build();

    public static final EventDTO BETON_HOFI_CONCERT_DTO = EventDTO.builder()
                                                            .eventId(ID_456)
                                                            .title(BETON_HOFI_88)
                                                            .startTimeStamp(AUG_8_20H.toInstant().toEpochMilli())
                                                            .endTimeStamp(AUG_8_22H.toInstant().toEpochMilli())
                                                            .location(BUDAPEST)
                                                            .build();
}