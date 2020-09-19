package com.bajera.xlog.trax.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static LocalDate calendarToLocalDate(Calendar calendar) {
        Date input = calendar.getTime();
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Calendar localDateToCalendar(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String formatLocalDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
    }

    public static boolean isSameMonth(LocalDate date, LocalDate otherDate) {
        return (date.getMonthValue() == otherDate.getMonthValue()
                && date.getYear() == otherDate.getYear());
    }

    public static boolean isSameWeek(LocalDate date, LocalDate otherDate) {
        TemporalField tf = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return (date.get(tf) == otherDate.get(tf) && date.getYear() == otherDate.getYear());
    }
}
