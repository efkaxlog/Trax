package com.bajera.xlog.trax.activities.ItemActivity.model;

import com.bajera.xlog.trax.data.db.model.Record;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChartData {
    private BarData bars;
    private ArrayList<String> labels;
    private int datasetColour;

    /**
     * @param dataset
     * @param makeEmptyBars if true, will add bars for days with no entry between days with entries
     */
    public ChartData(List<Record> dataset, boolean makeEmptyBars, String grouping, int datasetColour) {
        this.datasetColour = datasetColour;
        List<Record> datasetForChart;
        if (makeEmptyBars) {
            datasetForChart = addEmptyBars(dataset);
        } else {
            datasetForChart = dataset;
        }
        // WARNING
        // Grouper.group() is called after adding empty bars to the dataset!
        // This means that it is working on more (maybe a LOT more) data than it must. // todo
        datasetForChart = Grouper.group(datasetForChart, grouping);
        makeBarData(datasetForChart);
        makeXAxisLabels(datasetForChart, grouping);
    }

    /**
     * @param dataset (required to be ordered by date asc. and no duplicate dates)
     * @return new ArrayList with added bars for days with no entry
     */
    private ArrayList<Record> addEmptyBars(List<Record> dataset) {
        ArrayList<Record> result = new ArrayList<>();
        if (!dataset.isEmpty()) {
            LocalDate earlyDate = dataset.get(0).getDate();
            LocalDate lastDate = dataset.get(dataset.size() - 1).getDate().plusDays(1);
            LocalDate currentDate = earlyDate;
            int i = 0;
            while (currentDate.compareTo(lastDate) != 0) {
                Record record = dataset.get(i);
                if (currentDate.compareTo(record.getDate()) == 0) {
                    result.add(record);
                    i++;
                } else {
                    result.add(new Record(0.0f, currentDate));
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        return result;

    }

    private void makeBarData(List<Record> records) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            entries.add(new BarEntry(i, records.get(i).getValue()));
        }
        BarDataSet barDataset = new BarDataSet(entries, null);
        barDataset.setValueTextSize(0);
        barDataset.setColor(datasetColour);
        bars = new BarData(barDataset);
    }

    private void makeXAxisLabels(List<Record> records, String grouping) {
        labels = new ArrayList<>();
        DateTimeFormatter formatter;
        if (grouping.equals("days") || grouping.equals("weeks")) {
            formatter = DateTimeFormatter.ofPattern("dd LLL");
        } else {
            formatter = DateTimeFormatter.ofPattern("LLL Y");
        }
        for (Record i : records) {
            labels.add(i.getDate().format(formatter));
        }
    }

    public BarData getBars() {
        return bars;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }
}
