package com.bajera.xlog.trax.activities.ItemActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applandeo.materialcalendarview.EventDay;
import com.bajera.xlog.trax.R;
import com.bajera.xlog.trax.activities.EditItemActivity.EditItemActivity;
import com.bajera.xlog.trax.activities.ItemActivity.model.ChartData;
import com.bajera.xlog.trax.data.AppDataManager;
import com.bajera.xlog.trax.databinding.ActivityItemBinding;
import com.bajera.xlog.trax.databinding.ActivityItemContentBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;


public class ItemActivity extends AppCompatActivity implements ItemActivityContract.View {
    private Context context;
    private ActivityItemContentBinding binding;
    private ItemActivityPresenter presenter;
    private BarChart barChart;
    private AlertDialog editDataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityItemBinding rootBinding = ActivityItemBinding.inflate(getLayoutInflater());
        View rootView = rootBinding.getRoot();
        setContentView(rootView);
        binding = rootBinding.activityItemContent;
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        barChart = findViewById(R.id.barChart);
        setupBarChart();
        setupEditDataDialog();
        binding.calendar.setOnDayClickListener(eventDay -> {
            presenter.onDataEditClick(eventDay.getCalendar());
        });
        RadioGroup rgViewSelector = findViewById(R.id.rg_view_selector);
        rgViewSelector.setOnCheckedChangeListener((radioGroup, checkedButtonId) -> {
            /**
             * A ViewSwitcher has only two views, so when a selection change is detected (in radio group with two buttons),
             * it's always safe to switch to the next view. If more views are needed, use ViewFlipper and modify this method.
             */
            binding.vsItemView.showNext();
        });

        long itemId = getIntent().getLongExtra("itemId", -1);
        presenter = new ItemActivityPresenter(this, new AppDataManager(context), itemId);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_quick_edit) {
            presenter.onDataEditClick(null);
        }
        if (id == R.id.action_item_settings) {
            presenter.onSettingsClick();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clearChart() {
        barChart.clear();
    }

    @Override
    public void showEditDataDialogNumbers(String title, String value, boolean removeButtonEnabled, boolean okButtonEnabled) {
        editDataDialog.setTitle(title);
        editDataDialog.show();
        EditText et = editDataDialog.findViewById(R.id.et_value);
        et.setVisibility(View.VISIBLE);
        et.setText(value);
        et.setSelection(et.getText().length());
        et.setError(null);
        editDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(okButtonEnabled);
        editDataDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(removeButtonEnabled);
    }

    @Override
    public void showEditDataDialogDaily(String title, boolean checkBoxTicked) {
        editDataDialog.setTitle(title);
        editDataDialog.show();
        editDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        editDataDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
        EditText et = editDataDialog.findViewById(R.id.et_value);
        et.setVisibility(View.GONE);
        CheckBox cb = editDataDialog.findViewById(R.id.cb_entry_done);
        cb.setVisibility(View.VISIBLE);
        cb.setChecked(checkBoxTicked);
    }

    @Override
    public String getEditDataValue() {
        EditText et = editDataDialog.findViewById(R.id.et_value);
        return et.getText().toString();
    }

    @Override
    public boolean getEditDataCheckBoxTicked() {
        CheckBox cb = editDataDialog.findViewById(R.id.cb_entry_done);
        return cb.isChecked();
    }

    @Override
    public void openEditItemActivity(long itemId) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("itemId", itemId);
        startActivity(intent);
    }

    @Override
    public void errorLoadingItem() {
        finish();
    }

    private void setupBarChart() {
        barChart.setNoDataText("No data");
        barChart.setDescription(null);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-65);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0.0f);
        barChart.getAxisRight().setEnabled(false);
    }

    private void setupEditDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout = getLayoutInflater().inflate(R.layout.dialog_edit_data, null);
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        EditText et = layout.findViewById(R.id.et_value);
        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            presenter.onDataEditConfirmClick();
        });
        builder.setNeutralButton("Remove record", (dialogInterface, i) -> {
            presenter.onRemoveRecordClick();
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.onValueInputChange(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        builder.setView(layout);
        editDataDialog = builder.create();
    }

    public void onGroupButtonClick(View view) {
        // todo: Instead of passing Radio Button text, find id of checked button and call e.g. presenter.onFooRadioButtonChecked().
        presenter.onGroupButtonClick(((RadioButton) view).getText().toString());
    }

    @Override
    public void setChartData(ChartData data) {
        barChart.notifyDataSetChanged();
        barChart.setData(data.getBars());
        XAxis xAxis = barChart.getXAxis();
        barChart.moveViewToX(data.getBars().getEntryCount()); // all the way to the right
        barChart.setVisibleXRange(14, 7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(data.getLabels()));
        xAxis.setLabelCount(data.getBars().getEntryCount());
        xAxis.setGranularity(1.0f);
        barChart.getLegend().setEnabled(false);
        barChart.setExtraBottomOffset(10.0f);
        barChart.invalidate();

    }

    @Override
    public void setCalendarMaxDate(Calendar date) {
        binding.calendar.setMaximumDate(date);
    }

    @Override
    public void showRecordAddSuccess() {

    }

    @Override
    public void showRecordEditSuccess() {

    }

    @Override
    public void disableDialogConfirmButton() {
        editDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        EditText et = editDataDialog.findViewById(R.id.et_value);
        et.setError(getString(R.string.invalid_input));
    }

    @Override
    public void enableDialogConfirmButton() {
        editDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        EditText et = editDataDialog.findViewById(R.id.et_value);
        et.setError(null);
    }

    // Passing Record ArrayList breaks the MVP pattern a little but what the hell.
    // todo: Implement a Resource Provider class, so the presenter can pass a ready list of EventDay with drawable ids.
    @Override
    public void setCalendarDayDrawables(ArrayList<EventDay> eventDays) {
        binding.calendar.setEvents(eventDays);
    }

    @Override
    public void setStatsValues(String today, String week, String month, String total) {
        // this is so tedious.
        View layoutDay = findViewById(R.id.layout_stats_day);
        View layoutWeek = findViewById(R.id.layout_stats_week);
        View layoutMonth = findViewById(R.id.layout_stats_month);
        View layoutTotal = findViewById(R.id.layout_stats_total);
        TextView tvDayTitle = layoutDay.findViewById(R.id.tv_tile_title);
        TextView tvWeekTitle = layoutWeek.findViewById(R.id.tv_tile_title);
        TextView tvMonthTitle = layoutMonth.findViewById(R.id.tv_tile_title);
        TextView tvTotalTitle = layoutTotal.findViewById(R.id.tv_tile_title);
        TextView tvDayValue = layoutDay.findViewById(R.id.tv_tile_value);
        TextView tvWeekValue = layoutWeek.findViewById(R.id.tv_tile_value);
        TextView tvMonthValue = layoutMonth.findViewById(R.id.tv_tile_value);
        TextView tvTotalValue = layoutTotal.findViewById(R.id.tv_tile_value);
        tvDayTitle.setText("Today");
        tvWeekTitle.setText("This week");
        tvMonthTitle.setText("This month");
        tvTotalTitle.setText("Total");
        tvDayValue.setText(today);
        tvWeekValue.setText(week);
        tvMonthValue.setText(month);
        tvTotalValue.setText(total);
    }

    @Override
    public void showRemoveRecordConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to remove this record?");
        builder.setPositiveButton("Yes", (dialog, which) -> presenter.onRemoveRecordConfirmClick());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void showRecordRemovedConfirmed() {
        Snackbar.make(binding.getRoot(), R.string.record_removed, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setGoalLayoutVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        View layout = findViewById(R.id.layout_goal);
        layout.setVisibility(visibility);
    }

    @Override
    public void setGoalLayoutTitle(String title) {
        TextView tvGoalLayoutTitle = findViewById(R.id.tv_goal);
        tvGoalLayoutTitle.setText(title);
    }

    @Override
    public void setGoalLayoutValues(int progressPercent, String progressPercentLabel,
                                    String goalProgressLabel, String goalType, String daysLeft) {
        ProgressBar bar = findViewById(R.id.pb_goal_progress);
        bar.setProgress(progressPercent);
        TextView tvProgressPercent = findViewById(R.id.tv_goal_progress_percent);
        tvProgressPercent.setText(progressPercentLabel);

        View progressTile = findViewById(R.id.layout_goal_progress_tile);
        TextView progressTileTitle = progressTile.findViewById(R.id.tv_tile_title);
        progressTileTitle.setText(goalType);
        TextView progressValue = progressTile.findViewById(R.id.tv_tile_value);
        progressValue.setText(goalProgressLabel);

        View daysLeftTile = findViewById(R.id.layout_goal_days_left_tile);
        TextView daysLeftTileTitle = daysLeftTile.findViewById(R.id.tv_tile_title);
        daysLeftTileTitle.setText("Days left");
        TextView daysLeftValue = daysLeftTile.findViewById(R.id.tv_tile_value);
        daysLeftValue.setText(daysLeft);
    }

    @Override
    public void setTitle(String name) {
        super.setTitle(name);
    }
}
