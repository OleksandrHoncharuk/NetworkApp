package com.example.networkaplication.home.search.story

import java.util.ArrayList

class SearchStoryPresenter(private var items: ArrayList<SearchItem>?) {

    internal val searchRowCount: Int
        get() = items!!.size

    internal fun onBindSearchStoryRowAtPosition(rowView: SearchStoryRowView, position: Int) {
        val item = items!![position]
        rowView.setSearchStoryTest(item.searchText!!)
    }

    internal fun getSearchItem(position: Int): SearchItem {
        return items!![position]
    }

    internal fun refreshSearch(refreshList: ArrayList<SearchItem>) {
        this.items = ArrayList(refreshList)
    }
}
