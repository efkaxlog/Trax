package com.bajera.xlog.trax.testing;

import com.bajera.xlog.trax.data.db.model.Record;
import com.github.mikephil.charting.data.Entry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class generating various data for testing purposes.
 */
public class DummyData {

    /**
     * @param size number of Entries
     * @return ArrayList<Entry> with random and valid data.
     */
    public static ArrayList<Entry> randomData(int size) {
        float range = 100.0f;
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            float randomNumber = new Random().nextFloat() * range;
            entries.add(new Entry(i, randomNumber));
        }
        return entries;
    }

    public static ArrayList<Record> randomItems(int size) {
        float valueRange = 10.0f;
        ArrayList<Record> records = new ArrayList<>();
        LocalDate date = LocalDate.now().minusDays(size);
        for (int i = 0; i < size; i++) {
            date = date.plusDays(1);
            if (Math.random() > 0.75) {
                date = date.plusDays(1);
            }
            if (Math.random() > 0.9) {
                date = date.plusDays(1);
            }
            records.add(new Record((float) Math.random() * valueRange, date));
        }
        return records;
    }
}
