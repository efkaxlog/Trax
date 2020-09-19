package com.bajera.xlog.trax.activities.ItemPickerActivity;

import com.bajera.xlog.trax.data.db.model.Item;

import java.util.List;

public interface ItemPickerActivityContract {
    interface View {
        void setupAdapter(List<Item> dataset);
        void launchItemActivity(long itemId);
        void onItemNotFound(String error);
        void showDeleteItemDialog(String title);
        void onItemDeletedSuccess(String message);
        void onItemDeletedFailed();
        void refreshDataset();
    }

    interface Presenter {
        void onItemClick(int position);
        void onItemLongClick(int position);
        void onItemDeleteClick();
        void onItemDeleteConfirm();
        void onResume();
    }
}
