package com.bajera.xlog.trax.activities.ItemPickerActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bajera.xlog.trax.R;
import com.bajera.xlog.trax.data.db.model.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private TextView textView;
    private ItemsAdapter.OnItemListener itemClickListener;

    public ItemViewHolder(View layout, ItemsAdapter.OnItemListener itemClickListener) {
        super(layout);
        textView = layout.findViewById(R.id.tv_item_name);
        this.itemClickListener = itemClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void bind(Item item) {
        textView.setText(item.getName());
    }

    @Override
    public void onClick(View view) {
        Log.d("ItemViewHolder", "On short click!");
        itemClickListener.onItemClick(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d("ItemViewHolder", "On long click!");
        itemClickListener.onItemLongClick(getAdapterPosition(), view);
        return true;
    }
}