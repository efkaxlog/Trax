package com.bajera.xlog.trax.activities.ItemActivity;

import com.applandeo.materialcalendarview.EventDay;
import com.bajera.xlog.trax.activities.ItemActivity.model.ChartData;

import java.util.ArrayList;
import java.util.Calendar;

public interface ItemActivityContract {
    interface View {
        void setChartData(ChartData data);
        void setCalendarMaxDate(Calendar date);
        void showRecordAddSuccess();
        void showRecordEditSuccess();
        void disableDialogConfirmButton();
        void enableDialogConfirmButton();
        void setCalendarDayDrawables(ArrayList<EventDay> eventDays);
        void setStatsValues(String today, String week, String month, String total);
        void showRemoveRecordConfirmationDialog();
        void showRecordRemovedConfirmed();
        void setGoalLayoutVisible(boolean visible);
        void setGoalLayoutTitle(String title);
        void setGoalLayoutValues(int progressPercent, String progressPercentLabel,
                                 String goalProgressLabel, String goalType, String daysLeft);
        void clearChart();
        void showEditDataDialogNumbers(String title, String value, boolean removeButtonEnabled, boolean okButtonEnabled);
        void showEditDataDialogDaily(String title, boolean checkBoxTicked);
        String getEditDataValue();
        boolean getEditDataCheckBoxTicked();
        void openEditItemActivity(long itemId);
        void errorLoadingItem();
        void setTitle(String name);
    }

    interface Presenter {
        void onDataEditClick(Calendar calendar);
        void onDataEditConfirmClick();
        void onValueInputChange(String text);
        void onGroupButtonClick(String text);
        void onRemoveRecordClick();
        void onRemoveRecordConfirmClick();
        void onSettingsClick();
        void onResume();
    }
}
