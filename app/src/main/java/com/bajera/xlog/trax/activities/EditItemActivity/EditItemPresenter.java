package com.bajera.xlog.trax.activities.EditItemActivity;

import com.bajera.xlog.trax.data.AppDataManager;
import com.bajera.xlog.trax.data.db.model.Item;

public class EditItemPresenter implements EditItemContract.Presenter {
    private EditItemContract.View view;
    private AppDataManager dataManager;
    private Item editingItem;
    private boolean inEditMode = false;

    /**
     * @param itemId id of the item to edit. If -1, presenter will be in creating new Item mode, else, in editing mode.
     */
    public EditItemPresenter(EditItemContract.View view, AppDataManager dataManager, long itemId) {
        this.view = view;
        this.dataManager = dataManager;

        editingItem = dataManager.getItemById(itemId);
        if (editingItem == null) {
            editingItem = new Item();
        } else {
            inEditMode = true;
        }
        configure();
    }

    private void configure() {
        // Using Enum.ordinal(). The View options order must match order of enum constants.
        String title = inEditMode ? "Edit..." : "Create new...";
        view.setTitle(title);
        view.setItemName(editingItem.getName());
        view.setGoalType(editingItem.getGoalType().ordinal());
        view.setGoalTrackingTypeEnabled(!inEditMode);
        view.setGoalTimeFrame(editingItem.getGoalTimeFrame().ordinal());
        String goalValue = editingItem.getGoalValue() > 0.0 ? String.valueOf(editingItem.getGoalValue()) : "";
        view.setGoalValue(goalValue);
        boolean goalInputEnabled = editingItem.getGoalType() != Item.GoalType.NONE;
        view.setGoalValueInputEnabled(goalInputEnabled);
        if (inEditMode) {
            view.setTrackingType(editingItem.getTrackingType().ordinal());
        }
    }

    /**
     * Applies input from view's input form to the edited item.
     */
    private void applyInput() {
        editingItem.setName(view.getName());
        int goalType = view.getGoalType();
        editingItem.setGoalType(goalType);
        editingItem.setGoalTimeFrame(view.getGoalTimeFrame());
        if (goalType == Item.GoalType.NONE.ordinal()) {
            editingItem.setGoalValue(0.0f);
        } else {
            editingItem.setGoalValue(view.getGoalValue());
        }
        if (!inEditMode) {
            editingItem.setTrackingType(view.getTrackingType());
        }
    }

    private boolean isInputValid() {
        return isValidName(view.getName()) && isValidGoalValue(view.getGoalValue());
    }

    private boolean isValidName(String itemName) {
        itemName = itemName.trim();
        Item queriedItem = dataManager.getItemByName(itemName);
        if (itemName.isEmpty()) {
            view.errorInvalidName("Name cannot be empty.");
            return false;
        } else if (!inEditMode && queriedItem != null) {
            view.errorInvalidName("Name is already taken.");
            return false;
        } else if (queriedItem != null && !queriedItem.getName().equals(itemName)) {
            // Editing an item name to a new name that already is in database.
            view.errorInvalidName("Name is already taken.");
            return false;
        }
        return true;
    }

    private boolean isValidGoalValue(String goalValueInput) {
        if (view.getGoalType() != Item.GoalType.NONE.ordinal()) {
            float goalValue;
            try {
                goalValue = Float.parseFloat(goalValueInput);
            } catch (NumberFormatException e) {
                view.errorInvalidGoalValue("Enter a number.");
                return false;
            }
            if (goalValue <= 0) {
                view.errorInvalidGoalValue("Target or limit must be more than 0.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSaveClick() {
        if (!isInputValid()) {
            return;
        }
        applyInput();
        if (inEditMode) {
            dataManager.updateItem(editingItem);
            view.onItemEdited();
        } else {
            long newItemId = dataManager.insertItem(editingItem);
            view.onNewItemCreated(newItemId);
        }
    }

    @Override
    public void onTrackingTypeSelected() {
        if (view.getTrackingType() == Item.TrackingType.ONCE_A_DAY.ordinal()) {
            view.displayGoalWarning();
        } else {
            view.hideGoalWarning();
        }
    }

    @Override
    public void onGoalTypeSelected() {
        if (view.getGoalType() == Item.GoalType.NONE.ordinal()) {
            view.setGoalValueInputEnabled(false);
        } else {
            view.setGoalValueInputEnabled(true);
        }
    }
}
