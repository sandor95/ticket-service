package hu.otp.ticket.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;

public class Const {

    public static final Charset ENCODING = StandardCharsets.UTF_8;

    public static final ZoneId BUDAPEST_ZONE_ID = ZoneId.of("Europe/Budapest");
}