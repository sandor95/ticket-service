package hu.otp.ticket.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

public class Const {

    public static final Charset ENCODING = StandardCharsets.UTF_8;

    public static final ZoneId BUDAPEST_ZONE_ID = ZoneId.of("Europe/Budapest");

    public static final String X_USER_TOKEN = "x-user-token";

    public static final String USER_ID_PARAM = "userid";

    public static final String EVENT_ID_PARAM = "eventid";

    public static final String SEAT_CODE_PARAM = "seatcode";

    public static final String CARD_ID_PARAM = "cardid";
}