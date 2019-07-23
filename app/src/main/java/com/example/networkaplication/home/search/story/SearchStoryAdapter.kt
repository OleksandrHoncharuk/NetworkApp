package com.example.networkaplication.home.search.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.networkaplication.R

import java.util.ArrayList

class SearchStoryAdapter(private val presenter: SearchStoryPresenter) : RecyclerView.Adapter<SearchStoryAdapter.SearchStoryViewHolder>() {
    private var onSearchItemClickedListener: OnSearchItemClickedListener? = null

    inner class SearchStoryViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), SearchStoryRowView, View.OnClickListener {
        internal val searchStory: TextView
        internal val remove: ImageView

        init {

            itemView.setOnClickListener(this)
            searchStory = itemView.findViewById(R.id.search_story)
            remove = itemView.findViewById(R.id.delete_query)
            remove.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onSearchItemClickedListener!!.onClickedSearchItem(v, presenter.getSearchItem(adapterPosition))
        }

        override fun setSearchStoryTest(text: String) {
            searchStory.text = text
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SearchStoryViewHolder {
        return SearchStoryViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.search_item_layout, null))
    }

    fun setOnSearchItemClickedListener(onSearchItemClickedListener: OnSearchItemClickedListener) {
        this.onSearchItemClickedListener = onSearchItemClickedListener
    }

    override fun onBindViewHolder(viewHolder: SearchStoryViewHolder, i: Int) {
        presenter.onBindSearchStoryRowAtPosition(viewHolder, i)
    }

    override fun getItemCount(): Int {
        return presenter.searchRowCount
    }

    interface OnSearchItemClickedListener {
        fun onClickedSearchItem(view: View, itemData: SearchItem)
    }

    fun refresh(items: ArrayList<SearchItem>) {
        presenter.refreshSearch(items)
    }
}