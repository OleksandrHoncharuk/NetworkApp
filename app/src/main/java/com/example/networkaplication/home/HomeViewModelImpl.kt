package com.example.networkaplication.home

import android.app.Activity
import android.app.Application
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel

import com.example.networkaplication.R
import com.example.networkaplication.details.DetailsViewFragment
import com.example.networkaplication.home.adapter.HomeAdapter
import com.example.networkaplication.home.adapter.ItemData
import com.example.networkaplication.home.adapter.ListPresenter
import com.example.networkaplication.home.search.story.SearchItem
import com.example.networkaplication.models.search.Search
import com.example.networkaplication.persistance.model.MovieQuery

import java.util.ArrayList

class HomeViewModelImpl internal constructor(application: Application, private val view: HomeContract.HomeView) : AndroidViewModel(application), HomeContract.HomeViewModel, HomeCallback {
    private val repository: HomeContract.HomeRepository
    private var searchItems = ArrayList<SearchItem>()
     var items = ArrayList<ItemData>()

    init {
        this.repository = HomeRepositoryImpl(application, this)
    }

    override fun onEditorAction(v: TextView, actionId: Int): Boolean {
        var result = false
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard(v)
            clearSearchAdapter()
            search(v.text.toString())
            result = true
        }
        return result
    }

    override fun setItemToDataAdapter(items: ArrayList<ItemData>) {
        val adapter = HomeAdapter(ListPresenter(this.items))
        adapter.setOnFilmClickedListener(view)
        view.setRecycleViewAdapter(adapter)
    }

    override fun refreshSearchAdapter() {
        if (searchItems.size == 0)
            view.getSearchRecycle().visibility = View.INVISIBLE
        else
            view.getSearchRecycle().visibility = View.VISIBLE

        view.setSearchAdapter(searchItems)
    }

    override fun clearSearchAdapter() {
        view.getSearchRecycle().visibility = View.INVISIBLE
        searchItems = ArrayList()
        view.setSearchAdapter(searchItems)
    }

    override fun createDetailsView(itemData: ItemData): DetailsViewFragment {
        return DetailsViewFragment.newInstance(itemData.title!!, itemData.omdbId!!)
    }

    override fun hideKeyboard(view: View) {
        val imm = this.view.getHomeViewActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)
                as InputMethodManager

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun search(searchRequest: String) {
        repository.startSearch(searchRequest)
    }

    override fun addUniqueMovie(title: String) {
        if (repository.findByTitle(title) == null) {
            val movieQuery = MovieQuery(view.getSearchText()
                    , System.currentTimeMillis())
            repository.insertIntoQuery(movieQuery)
        }
    }

    override fun onTextChanged(sequence: CharSequence, isSearchPopped: Boolean) {
        clearSearchAdapter()
        if (isSearchPopped) {
            val movies = ArrayList(
                    repository.findAllFromQuery("$sequence%"))

            for (movie in movies) {
                searchItems.add(SearchItem(movie.name))
            }

            refreshSearchAdapter()
        } else
            HomeViewFragment.setIsSearchViewPopped(true)
    }

    override fun onFocusChanged(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            refreshSearchAdapter()
        } else {
            clearSearchAdapter()
            hideKeyboard(view)
        }
    }

    override fun onClickedSearchItem(viewId: Int, itemData: SearchItem) =
            if (viewId == R.id.delete_query) {
                searchItems.remove(itemData)
                refreshSearchAdapter()
            } else {
                view.setSearchText(itemData.searchText!!)
                clearSearchAdapter()
            }

    override fun onSearchReceived(search: Search) {
        val title = view.getSearchText()
        addUniqueMovie(title)

        val data = ArrayList<ItemData>()

        for (`object` in search.search!!) {
            val item = ItemData(
                    `object`.poster,
                    `object`.title,
                    `object`.imdbID)

            data.add(item)
        }

        items = data
//        view.setBundleFromSearch(search.search!!)
        view.setItemDataToAdapter(data)
    }
}
