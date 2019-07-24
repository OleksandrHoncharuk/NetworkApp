package com.example.networkaplication.home

import android.text.TextWatcher
import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.networkaplication.MainActivity
import com.example.networkaplication.details.DetailsViewFragment
import com.example.networkaplication.home.adapter.HomeAdapter
import com.example.networkaplication.home.adapter.ItemData
import com.example.networkaplication.home.search.story.SearchItem
import com.example.networkaplication.home.search.story.SearchStoryAdapter
import com.example.networkaplication.models.search.SearchObject
import com.example.networkaplication.persistance.model.MovieQuery

import java.util.ArrayList

interface HomeContract {

    interface HomeView : HomeAdapter.OnFilmClickedListener, SearchStoryAdapter.OnSearchItemClickedListener, TextView.OnEditorActionListener, TextWatcher, View.OnFocusChangeListener {

        fun getHomeViewActivity(): MainActivity

        fun getSearchRecycle(): RecyclerView

        fun getSearchText(): String

        fun setSearchText(text: String)

        fun setRecycleViewAdapter(adapter: HomeAdapter)
    }

    interface HomeViewModel {

        fun onEditorAction(v: TextView, actionId: Int): Boolean

        fun setItemToDataAdapter()

        fun refreshSearchAdapter()

        fun createDetailsView(itemData: ItemData): DetailsViewFragment

        fun clearSearchAdapter()

        fun hideKeyboard(view: View)

        fun search(searchRequest: String)

        fun addUniqueMovie(title: String)

        fun onTextChanged(sequence: CharSequence, isSearchPopped: Boolean)

        fun onFocusChanged(view: View, hasFocus: Boolean)

        fun onClickedSearchItem(viewId: Int, itemData: SearchItem)
    }

    interface HomeRepository {
        fun startSearch(searchRequest: String)

        fun findAllFromQuery(search: String): List<MovieQuery>

        fun findByTitle(title: String): MovieQuery

        fun insertIntoQuery(query: MovieQuery)
    }
}
