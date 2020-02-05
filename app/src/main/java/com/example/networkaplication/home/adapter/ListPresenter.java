package com.example.networkaplication.home.adapter;

import java.util.ArrayList;

public class ListPresenter {
    private final ArrayList<ItemData> items;

    public ListPresenter(ArrayList<ItemData> items) { this.items = items; }

    public void onBindRowViewAtPosition(RowView rowView, int position) {
        ItemData item = items.get(position);
        rowView.setImage(item.getImageUrl());
        rowView.setTitle(item.getTitle());
    }

    public int getRowsCount() {
        return items.size();
    }

    public ItemData get(int position) {
        return items.get(position);
    }
}
