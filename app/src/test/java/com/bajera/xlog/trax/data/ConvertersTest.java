package com.bajera.xlog.trax.data;


import com.bajera.xlog.trax.data.db.model.Converters;

import org.junit.Test;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ConvertersTest {

    @Test
    public void dateFromString() {
        String dateString = "1990-01-31";
        LocalDate expected = LocalDate.of(1990, 01, 31);
        LocalDate result = Converters.dateFromString(dateString);
        assertEquals(expected, result);
    }

    @Test
    public void stringFromDate() {
        LocalDate date = LocalDate.of(1990, 01, 31);
        String expected = "1990-01-31";
        String result = Converters.stringFromDate(date);
        assertEquals(expected, result);
    }
}
