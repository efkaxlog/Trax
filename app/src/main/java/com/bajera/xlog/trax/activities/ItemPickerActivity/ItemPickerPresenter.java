package com.bajera.xlog.trax.activities.ItemPickerActivity;

import com.bajera.xlog.trax.data.AppDataManager;
import com.bajera.xlog.trax.data.db.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemPickerPresenter implements ItemPickerActivityContract.Presenter {
    private ItemPickerActivityContract.View view;
    private AppDataManager dataManager;
    private List<Item> dataset;
    private Item itemSelected = null;

    public ItemPickerPresenter(ItemPickerActivityContract.View view, AppDataManager dataManager) {
        this.view = view;
        this.dataManager = dataManager;
        dataset = new ArrayList<>();
        view.setupAdapter(dataset);
    }

    @Override
    public void onResume() {
        dataset.clear();
        dataset.addAll(dataManager.getAllItems());
        view.refreshDataset();
    }

    @Override
    public void onItemClick(int position) {
        Item item = dataset.get(position);
        if (dataManager.getItemById(item.getId()) != null) {
            view.launchItemActivity(item.getId());
        } else {
            view.onItemNotFound("Item was not found in the database."); // shouldn't happen, but in case.
        }
    }

    @Override
    public void onItemLongClick(int position) {
        itemSelected = dataset.get(position);
    }

    @Override
    public void onItemDeleteClick() {
        if (itemSelected != null) {
            String title = "Delete " + itemSelected.getName();
            view.showDeleteItemDialog(title);
        }
    }

    @Override
    public void onItemDeleteConfirm() {
        if (itemSelected != null) {
            dataManager.deleteItem(itemSelected);
            view.onItemDeletedSuccess("\"" + itemSelected.getName() + "\" deleted.");
            dataset.remove(itemSelected);
            itemSelected = null;
        } else {
            view.onItemDeletedFailed();
        }
    }
}
