package com.bajera.xlog.trax.data;

import com.bajera.xlog.trax.activities.ItemActivity.model.ChartData;
import com.bajera.xlog.trax.data.db.model.Record;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChartDataTest {

    private ArrayList<Record> dataset = new ArrayList<>();

    @Before
    public void generateSampleData() {
        dataset.add(new Record(1.0f, LocalDate.of(2020, 1,1))); // Wed
        dataset.add(new Record(1.0f, LocalDate.of(2020, 1,2))); // Thu
        dataset.add(new Record(1.0f, LocalDate.of(2020, 1,3))); // Fri
        dataset.add(new Record(1.0f, LocalDate.of(2020, 2,10))); // Mon
        dataset.add(new Record(1.0f, LocalDate.of(2020, 2,11))); // Tue
    }

    @Test
    public void datasetIntoChartData() {
        ChartData chartData = new ChartData(dataset, false, "days", 0);
        assertEquals(chartData.getBars().getEntryCount(), dataset.size());
    }

    @Test
    public void xAxisLabelsDailyOrWeeklyGrouping() {
        ChartData chartData = new ChartData(dataset, false, "days", 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLL");
        String expected = dataset.get(0).getDate().format(formatter);
        String label = chartData.getLabels().get(0);
        assertEquals(expected, label);
    }

    @Test
    public void xAxisLabelsWeeklyGrouping() {
        ChartData chartData = new ChartData(dataset, false, "months", 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLL");
        String expected = dataset.get(0).getDate().format(formatter);
        String label = chartData.getLabels().get(0);
        assertEquals(expected, label);
    }

    @Test
    public void addingEmptyBarsTest() {
        ChartData chartData = new ChartData(dataset, true, "months", 0);
        int expected = 42; // 5 (dataset size) + 37 (37 empty days between entries)
        assertEquals(expected, chartData.getBars().getDataSetCount());
    }
}