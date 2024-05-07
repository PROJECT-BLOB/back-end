package com.codeit.blob.global.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String timeZone = "Asia/Seoul";

    private DateTimeUtils() {}

    public static String format(LocalDateTime dateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of(timeZone));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }

}
