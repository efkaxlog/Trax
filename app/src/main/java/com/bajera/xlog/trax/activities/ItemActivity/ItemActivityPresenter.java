package com.bajera.xlog.trax.activities.ItemActivity;

import com.applandeo.materialcalendarview.EventDay;
import com.bajera.xlog.trax.activities.ItemActivity.model.ChartData;
import com.bajera.xlog.trax.data.AppDataManager;
import com.bajera.xlog.trax.data.db.model.Item;
import com.bajera.xlog.trax.data.db.model.Item.TrackingType;
import com.bajera.xlog.trax.data.db.model.Record;
import com.bajera.xlog.trax.util.DateUtils;
import com.bajera.xlog.trax.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ItemActivityPresenter implements ItemActivityContract.Presenter {
    private ItemActivityContract.View view;
    private AppDataManager dataManager;
    private Item item;
    private long itemId;
    private List<Record> dataset = new ArrayList<>();
    private int calendarDrawableId;
    private String grouping = "days";
    private boolean addEmptyBars = true;
    private LocalDate editDataDay; // for use with the edit data dialog
    private Record recordToEdit;


    public ItemActivityPresenter(ItemActivityContract.View view, AppDataManager dataManager, long itemId) {
        this.view = view;
        this.dataManager = dataManager;
        this.itemId = itemId;
    }

    @Override
    public void onResume() {
        item = dataManager.getItemById(itemId);
        if (item == null) {
            view.errorLoadingItem();
        }
        view.setTitle(item.getName());
        view.setCalendarMaxDate(Calendar.getInstance()); // The user shouldn't be able to add any data for days later than today.
        calendarDrawableId = getCalendarDrawableId();
        reloadDataset();
    }

    private void setupGoalView() {
        if (item.getGoalType() == Item.GoalType.NONE) {
            view.setGoalLayoutVisible(false);
        } else {
            view.setGoalLayoutVisible(true);
            LocalDate goalStartDate = LocalDate.now();
            switch (item.getGoalTimeFrame()) {
                case WEEKLY:
                    goalStartDate = goalStartDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    break;
                case MONTHLY:
                    goalStartDate = LocalDate.now().withDayOfMonth(1);
                    break;
                default:
                    break;
            }
            List<Record> records = dataManager.getRecordsBetweenDates(item.getId(), goalStartDate, LocalDate.now());
            float progressValue = item.getGoalProgress(records);
            int progressPercent = item.getGoalProgressPercent(progressValue);
            view.setGoalLayoutTitle("GOAL - " + item.getGoalSummary());
            view.setGoalLayoutValues(progressPercent, progressPercent + "%",
                    item.getGoalProgressLabel(progressValue), item.goalTypeToString(), item.getGoalDaysLeftLabel());
        }
    }

    private void setStatsValues() {
        float today = 0.0f;
        float week = 0.0f;
        float month = 0.0f;
        float total = 0.0f;
        LocalDate todayDate = LocalDate.now();
        for (Record r : dataset) {
            float value = r.getValue();
            LocalDate rDate = r.getDate();
            total += value;
            if (DateUtils.isSameMonth(todayDate, rDate)) {
                month += value;
            }
            if (DateUtils.isSameWeek(todayDate, rDate)) {
                week += value;
            }
            if (todayDate.compareTo(rDate) == 0) {
                today += value;
            }
        }
        view.setStatsValues(StringUtils.format(today), StringUtils.format(week),
                StringUtils.format(month), StringUtils.format(total));
    }

    private ArrayList<EventDay> makeCalendarLabels() {
        ArrayList<EventDay> eventDays = new ArrayList<>();
        if (calendarDrawableId != 0) {
            for (Record r : dataset) {
                eventDays.add(makeEventDay(r.getDate(), calendarDrawableId));
            }
        }
        return eventDays;
    }

    private EventDay makeEventDay(LocalDate date, int drawable) {
        return new EventDay(DateUtils.localDateToCalendar(date), drawable);
    }

    private int getCalendarDrawableId() {
        return dataManager.getCalendarDailyTaskDrawableId();
    }

    /**
     * Should be called whenever there was a change in the database e.g. adding, editing or removing a record.
     */
    private void reloadDataset() {
        dataset = dataManager.getRecordsByItemId(item.getId());
        if (dataset.isEmpty()) {
            view.clearChart();
        } else {
            ChartData chartData = new ChartData(dataset, addEmptyBars, grouping, dataManager.getChartDatasetColour());
            view.setChartData(chartData);
        }

        view.setCalendarDayDrawables(makeCalendarLabels());
        setStatsValues();
        setupGoalView();
    }

    private void showEditDataDialogNumbers(String title, Record record) {
        if (record == null) {
            view.showEditDataDialogNumbers(title, "", false, false);
        } else {
            view.showEditDataDialogNumbers(title, StringUtils.format(record.getValue()), true, true);
        }
    }

    private void showEditDataDialogDaily(String title, Record record) {
        boolean checkBoxTicked = false;
        if (record != null && record.getValue() == 1.0f) {
            checkBoxTicked = true;
        }
        view.showEditDataDialogDaily(title, checkBoxTicked);
    }

    private void deleteRecord(Record record) {
        dataManager.deleteRecord(recordToEdit);
        reloadDataset();
        view.showRecordRemovedConfirmed();
    }

    /**
     * View calls this when user clicks the edit data button.
     *
     * @param calendar Day for when to edit the data. Can be null, it will mean today's date.
     */
    @Override
    public void onDataEditClick(Calendar calendar) {
        if (calendar == null) {
            editDataDay = LocalDate.now();
        } else {
            editDataDay = DateUtils.calendarToLocalDate(calendar);
        }
        if (!editDataDay.isAfter(LocalDate.now())) {
            String title = item.getName() + " - " + DateUtils.formatLocalDate(editDataDay);
            List<Record> records = dataManager.getRecordsByDate(item.getId(), editDataDay);
            if (!records.isEmpty()) {
                recordToEdit = records.get(0);
            } else {
                recordToEdit = null;
            }
            if (item.getTrackingType() == Item.TrackingType.NUMBERS) {
                showEditDataDialogNumbers(title, recordToEdit);
            } else if (item.getTrackingType() == Item.TrackingType.ONCE_A_DAY) {
                showEditDataDialogDaily(title, recordToEdit);
            }
        }
    }

    @Override
    public void onDataEditConfirmClick() {
        float value = 0.0f;
        if (item.getTrackingType() == TrackingType.NUMBERS) {
            value = Float.valueOf(view.getEditDataValue());
        } else if (item.getTrackingType() == TrackingType.ONCE_A_DAY && view.getEditDataCheckBoxTicked()) {
            value = 1.0f;
        }
        List<Record> records = dataManager.getRecordsByDate(item.getId(), editDataDay);
        if (records.isEmpty()) {
            if (value != 0.0f) {
                Record record = new Record(value, editDataDay, item.getId());
                dataManager.insertRecord(record);
                reloadDataset();
                view.showRecordAddSuccess();
            }
        } else {
            recordToEdit = records.get(0);
            if (value == 0.0f) {
                deleteRecord(recordToEdit);
            } else {
                if (recordToEdit.getValue() != value) {
                    recordToEdit.setValue(value);
                    dataManager.updateRecord(recordToEdit);
                    reloadDataset();
                }
            }
            view.showRecordEditSuccess();
        }
    }

    @Override
    public void onValueInputChange(String text) {
        try {
            Float.valueOf(text);
        } catch (NumberFormatException e) {
            view.disableDialogConfirmButton();
            return;
        }
        view.enableDialogConfirmButton();
    }

    @Override
    public void onGroupButtonClick(String grouping) {
        if (!dataset.isEmpty()) {
            ChartData chartData = new ChartData(dataset, addEmptyBars, grouping.toLowerCase(), dataManager.getChartDatasetColour());
            view.setChartData(chartData);
        }
    }

    @Override
    public void onRemoveRecordClick() {
        view.showRemoveRecordConfirmationDialog();
    }

    @Override
    public void onRemoveRecordConfirmClick() {
        deleteRecord(recordToEdit);
        recordToEdit = null;
    }

    @Override
    public void onSettingsClick() {
        view.openEditItemActivity(item.getId());
    }
}
