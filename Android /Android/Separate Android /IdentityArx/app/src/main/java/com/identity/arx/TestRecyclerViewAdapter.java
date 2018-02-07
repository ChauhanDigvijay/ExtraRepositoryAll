package com.identity.arx;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

public class TestRecyclerViewAdapter extends Adapter<ViewHolder> {
    static final int TYPE_CELL = 1;
    static final int TYPE_HEADER = 0;
    List<Object> contents;

    public TestRecyclerViewAdapter(List<Object> contents) {
        this.contents = contents;
    }

    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            default:
                return 1;
        }
    }

    public int getItemCount() {
        return this.contents.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false)) {
                };
            case 1:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false)) {
                };
            default:
                return null;
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
        }
    }
}
