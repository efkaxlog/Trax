package com.bajera.xlog.trax.data;

import com.bajera.xlog.trax.activities.ItemActivity.model.Grouper;
import com.bajera.xlog.trax.data.db.model.Record;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GrouperTest {
    private ArrayList<Record> dataset = getDataset();

    @Test
    public void groupByDays() {
        // Grouping daily returns the same.
        assertThat(Grouper.group(dataset, "days"), is(dataset));
    }

    @Test
    public void groupByWeeks() {
        ArrayList<Record> grouped = Grouper.group(dataset, "weeks");
        assertThat(grouped.size(), is(5));
        assertThat(grouped.get(0).getValue(), is(7.0f));
        assertThat(grouped.get(1).getValue(), is(2.0f));
        assertThat(grouped.get(2).getValue(), is(3.0f));
        assertThat(grouped.get(3).getValue(), is(2.0f));
        assertThat(grouped.get(4).getValue(), is(2.0f));
    }

    @Test
    public void groupByMonths() {
        ArrayList<Record> grouped = Grouper.group(dataset, "months");
        assertThat(grouped.size(), is(2));
        assertThat(grouped.get(0).getDate().getMonth(), is(Month.AUGUST));
        assertThat(grouped.get(0).getValue(), is(12.0f));
        assertThat(grouped.get(1).getDate().getMonth(), is(Month.SEPTEMBER));
        assertThat(grouped.get(1).getValue(), is(4.0f));
    }


    private ArrayList<Record> getDataset() {
        ArrayList<Record> dataset = new ArrayList<>();
        // Month 0 August
        // Week 0
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 3))); // Monday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 4))); // Tuesday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 5))); // Wednesday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 6))); // Thursday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 7))); // Friday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 8))); // Saturday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 9))); // Sunday
        // Week 1
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 10))); // Monday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 16))); // Sunday
        // Week 2
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 17))); // Monday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 19))); // Wednesday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 8, 22))); // Saturday
        // Month 1 September
        // Week 0
        dataset.add(new Record(1.0f, LocalDate.of(2020, 9, 1))); // Tuesday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 9, 6))); // Sunday
        // Week 1
        dataset.add(new Record(1.0f, LocalDate.of(2020, 9, 29))); // Tuesday
        dataset.add(new Record(1.0f, LocalDate.of(2020, 9, 30))); // Wednesday
        return dataset;
    }
}