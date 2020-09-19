package com.bajera.xlog.trax.activities.EditItemActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bajera.xlog.trax.R;
import com.bajera.xlog.trax.activities.ItemActivity.ItemActivity;
import com.bajera.xlog.trax.data.AppDataManager;
import com.bajera.xlog.trax.databinding.ActivityEditItemBinding;
import com.bajera.xlog.trax.databinding.ActivityEditItemContentBinding;

public class EditItemActivity extends AppCompatActivity implements EditItemContract.View {
    private Context context;
    private EditItemContract.Presenter presenter;
    private ActivityEditItemBinding root;
    private ActivityEditItemContentBinding content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = ActivityEditItemBinding.inflate(getLayoutInflater());
        View rootView = root.getRoot();
        setContentView(rootView);
        content = root.activityEditItemContent;
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        long itemId = getIntent().getLongExtra("itemId", -1);
        presenter = new EditItemPresenter(this, new AppDataManager(context), itemId);

        content.btnCancelItemEdit.setOnClickListener(view -> {
            finish();
        });

        content.rgGoalType.setOnCheckedChangeListener((radioGroup, i) ->
                presenter.onGoalTypeSelected());

        content.rgTrackingType.setOnCheckedChangeListener((radioGroup, i) ->
                presenter.onTrackingTypeSelected());

        content.btnSaveItem.setOnClickListener(view ->
                presenter.onSaveClick());

    }

    @Override
    public void onItemEdited() {
        finish();
    }

    @Override
    public void onNewItemCreated(long itemId) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("itemId", itemId);
        startActivity(intent);
        finish();
    }

    @Override
    public void errorInvalidName(String error) {
        content.etEditItemName.setError(error);
    }

    @Override
    public void errorInvalidGoalValue(String error) {
        content.etGoalValueInput.setError(error);
    }

    @Override
    public void displayGoalWarning() {
        content.tvGoalWarning.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGoalWarning() {
        content.tvGoalWarning.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title); // Calling without super caused infinite recursion lol.
    }

    @Override
    public void setItemName(String itemName) {
        content.etEditItemName.setText(itemName);
    }

    @Override
    public void setGoalValueInputEnabled(boolean enabled) {
        content.etGoalValueInput.setEnabled(enabled);
        content.rbGoalDaily.setEnabled(enabled);
        content.rbGoalWeekly.setEnabled(enabled);
    }

    @Override
    public void setGoalTrackingTypeEnabled(boolean enabled) {
        int visibility = enabled ? View.VISIBLE : View.GONE;
        content.tvHowToKeepTrack.setVisibility(visibility);
        content.rgTrackingType.setVisibility(visibility);
    }

    @Override
    public String getName() {
        return content.etEditItemName.getText().toString();
    }

    @Override
    public int getTrackingType() {
        int id = content.rgTrackingType.getCheckedRadioButtonId();
        RadioButton selected = findViewById(id);
        return content.rgTrackingType.indexOfChild(selected);
    }

    @Override
    public int getGoalType() {
        int id = content.rgGoalType.getCheckedRadioButtonId();
        RadioButton selected = findViewById(id);
        return content.rgGoalType.indexOfChild(selected);
    }

    @Override
    public int getGoalTimeFrame() {
        int id = content.rgGoalTimeFrame.getCheckedRadioButtonId();
        RadioButton selected = findViewById(id);
        return content.rgGoalTimeFrame.indexOfChild(selected);
    }

    @Override
    public String getGoalValue() {
        return content.etGoalValueInput.getText().toString();
    }

    @Override
    public void setName(String name) {
        content.etEditItemName.setText(name);
    }

    @Override
    public void setTrackingType(int index) {
        ((RadioButton) content.rgTrackingType.getChildAt(index)).setChecked(true);
    }

    @Override
    public void setGoalType(int index) {
        ((RadioButton) content.rgGoalType.getChildAt(index)).setChecked(true);
    }

    @Override
    public void setGoalTimeFrame(int index) {
        ((RadioButton) content.rgGoalTimeFrame.getChildAt(index)).setChecked(true);
    }

    @Override
    public void setGoalValue(String goalValue) {
        content.etGoalValueInput.setText(goalValue);
    }


}
