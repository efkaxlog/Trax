package com.bajera.xlog.trax.activities.ItemActivity.model;

import com.bajera.xlog.trax.data.db.model.Record;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class used for grouping Record objects.
 */
public class Grouper {

    /**
     * @param records  Sorted list of records to group. Will not group correctly if the list is not sorted.
     * @param grouping int indicating what to group by (by week, month etc.)
     */
    public static ArrayList<Record> group(List<Record> records, String grouping) {
        if (records.size() > 0) {
            grouping = grouping.toLowerCase();
            if (grouping.equals("weeks")) {
                return groupByWeeks(records);
            } else if (grouping.equals("months")) {
                return groupByMonths(records);
            }
        }
        return new ArrayList<>(records);
    }

    private static ArrayList<Record> groupByWeeks(List<Record> records) {
        ArrayList<Record> groups = new ArrayList<>();
        WeekFields weekFields = WeekFields.of(Locale.UK); // Locale.UK with first day of week being Monday.
        Record group = new Record(0.0f, records.get(0).getDate());
        for (Record record : records) {
            LocalDate groupDate = group.getDate();
            LocalDate itemDate = record.getDate();
            int groupWeek = groupDate.get(weekFields.weekOfWeekBasedYear());
            int itemWeek = itemDate.get(weekFields.weekOfWeekBasedYear());
            if (groupWeek == itemWeek && groupDate.getYear() == itemDate.getYear()) {
                group.addValue(record.getValue());
            } else {
                groups.add(group);
                group = new Record(record.getValue(), record.getDate());
            }
        }
        groups.add(group);
        return groups;
    }


    private static ArrayList<Record> groupByMonths(List<Record> records) {
        ArrayList<Record> groups = new ArrayList<>();
        Record group = new Record(0.0f, records.get(0).getDate());
        for (Record record : records) {
            LocalDate groupDate = group.getDate();
            LocalDate itemDate = record.getDate();
            if (groupDate.getMonth() == itemDate.getMonth() && groupDate.getYear() == itemDate.getYear()) {
                group.addValue(record.getValue());
            } else {
                groups.add(group);
                group = new Record(record.getValue(), record.getDate());
            }
        }
        groups.add(group);
        return groups;
    }
}
