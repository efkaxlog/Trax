package com.bajera.xlog.trax.activities.EditItemActivity;

public interface EditItemContract {
    interface View {
        void onItemEdited();
        void onNewItemCreated(long itemId);
        void errorInvalidName(String error);
        void errorInvalidGoalValue(String error);
        void displayGoalWarning();
        void hideGoalWarning();
        String getName();
        int getTrackingType();
        int getGoalType();
        int getGoalTimeFrame();
        String getGoalValue();
        void setTitle(String title);
        void setItemName(String itemName);
        void setName(String name);
        void setTrackingType(int index);
        void setGoalType(int index);
        void setGoalTimeFrame(int index);
        void setGoalValue(String goalValue);
        void setGoalValueInputEnabled(boolean enabled);
        void setGoalTrackingTypeEnabled(boolean enabled);
    }

    interface Presenter {
        void onTrackingTypeSelected();
        void onGoalTypeSelected();
        void onSaveClick();
    }
}
