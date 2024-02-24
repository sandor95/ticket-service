package hu.otp.partner.knownobject;

import static hu.otp.ticket.service.Const.BUDAPEST_ZONE_ID;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Dates {

    public static final ZonedDateTime AUG_8_20H = ZonedDateTime.of(LocalDateTime.of(2024, 8, 8, 20, 0, 0), BUDAPEST_ZONE_ID);

    public static final ZonedDateTime AUG_8_22H = ZonedDateTime.of(LocalDateTime.of(2024, 8, 8, 22, 0, 0), BUDAPEST_ZONE_ID);

    public static final ZonedDateTime MAY_25 = ZonedDateTime.of(LocalDateTime.of(2024, 5, 25, 20, 0, 0), BUDAPEST_ZONE_ID);

    public static final ZonedDateTime MAY_26 = ZonedDateTime.of(LocalDateTime.of(2024, 5, 26, 23, 59, 59), BUDAPEST_ZONE_ID);
}