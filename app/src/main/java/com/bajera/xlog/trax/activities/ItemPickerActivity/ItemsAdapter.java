package com.bajera.xlog.trax.activities.ItemPickerActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bajera.xlog.trax.R;
import com.bajera.xlog.trax.data.db.model.Item;

import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private List<Item> dataset;
    private OnItemListener itemClickListener;

    public ItemsAdapter(List<Item> dataset, OnItemListener itemClickListener) {
        this.dataset = dataset;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_holder, parent, false);
        return new ItemViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int i) {
        holder.bind(dataset.get(i));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    interface OnItemListener {
        void onItemClick(int position);

        void onItemLongClick(int position, View itemView);
    }
}
