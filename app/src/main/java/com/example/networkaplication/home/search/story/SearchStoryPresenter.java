package com.example.networkaplication.home.search.story;

import java.util.ArrayList;

public class SearchStoryPresenter {

    private ArrayList<SearchItem> items;

    public SearchStoryPresenter(ArrayList<SearchItem> items) {
        this.items = items;
    }

    public void onBindSearchStoryRowAtPosition(SearchStoryRowView rowView, int position) {
        SearchItem item = items.get(position);
        rowView.setSearchStoryTest(item.getSearchText());
    }

    public int getSearchRowCount() {
        return items.size();
    }

    public SearchItem getSearchItem(int position) {
        return items.get(position);
    }

    public void refreshSearch(ArrayList<SearchItem> refreshList) {
        this.items = new ArrayList<>(refreshList);
    }
}
