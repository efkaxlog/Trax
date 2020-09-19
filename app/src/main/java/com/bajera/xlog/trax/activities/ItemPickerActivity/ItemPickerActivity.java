package com.bajera.xlog.trax.activities.ItemPickerActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bajera.xlog.trax.R;
import com.bajera.xlog.trax.activities.EditItemActivity.EditItemActivity;
import com.bajera.xlog.trax.activities.ItemActivity.ItemActivity;
import com.bajera.xlog.trax.data.AppDataManager;
import com.bajera.xlog.trax.data.db.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ItemPickerActivity extends AppCompatActivity implements ItemPickerActivityContract.View, ItemsAdapter.OnItemListener {

    private Context context;
    private ItemPickerPresenter presenter;
    private RecyclerView recyclerView;
    private ItemsAdapter adapter;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_picker);
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.rv_items);
        mainLayout = findViewById(R.id.layout_item_picker_activity);
        presenter = new ItemPickerPresenter(this, new AppDataManager(context));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_picker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_item) {
            Intent intent = new Intent(this, EditItemActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupAdapter(List<Item> dataset) {
        adapter = new ItemsAdapter(dataset, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void launchItemActivity(long itemId) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("itemId", itemId);
        startActivity(intent);
    }

    @Override
    public void onItemNotFound(String error) {

    }

    @Override
    public void onItemClick(int position) {
        presenter.onItemClick(position);
    }

    @Override
    public void onItemLongClick(int position, View itemView) {
        PopupMenu popup = new PopupMenu(this, itemView);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_item_options, popup.getMenu());
        popup.show();
        presenter.onItemLongClick(position);
    }

    public void onDeleteItemClick(MenuItem item) {
        presenter.onItemDeleteClick();
    }

    @Override
    public void showDeleteItemDialog(String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("Are you sure you want to delete this item along with all its entries permanently?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    presenter.onItemDeleteConfirm();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onItemDeletedSuccess(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDeletedFailed() {
        Snackbar.make(mainLayout, "Something went wrong, failed to delete item.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void refreshDataset() {
        adapter.notifyDataSetChanged();
    }
}