package com.bajera.xlog.trax.data;

import com.bajera.xlog.trax.data.db.model.Record;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RecordTest {

    Record example = new Record(0.0f, LocalDate.of(2000, 1, 1));

    @Test
    public void compareToBeforeDate() {
        Record record = new Record(0.0f, LocalDate.of(1999, 1, 1));
        assertThat(example.compareTo(record), is(1));
    }

    @Test
    public void compareToAfterDate() {
        Record record = new Record(0.0f, LocalDate.of(2001, 1, 1));
        assertThat(example.compareTo(record), is(-1));
    }

    @Test
    public void compareToSameDate() {
        Record record = new Record(0.0f, LocalDate.of(2000, 1, 1));
        assertThat(example.compareTo(record), is(0));
    }
}