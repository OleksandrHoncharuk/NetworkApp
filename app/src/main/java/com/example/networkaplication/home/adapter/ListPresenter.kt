package com.example.networkaplication.home.adapter

import java.util.ArrayList

class ListPresenter(private val items: ArrayList<ItemData>) {

    val rowsCount: Int
        get() = items.size

    fun onBindRowViewAtPosition(rowView: RowView, position: Int) {
        val item = items[position]
        rowView.setImage(item.imageUrl!!)
        rowView.setTitle(item.title!!)
    }

    operator fun get(position: Int): ItemData {
        return items[position]
    }
}
