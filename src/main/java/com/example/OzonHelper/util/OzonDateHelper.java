package com.example.OzonHelper.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class OzonDateHelper {
    public String[] getUtcInterval(LocalDate from, LocalDate to) {
        OffsetDateTime dateFrom = OffsetDateTime.of(from, LocalTime.MIN, ZoneOffset.UTC);
        OffsetDateTime dayTo = OffsetDateTime.of(to.plusDays(1), LocalTime.MIN, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return new String[]{formatter.format(dateFrom), formatter.format(dayTo)};
    }
}
