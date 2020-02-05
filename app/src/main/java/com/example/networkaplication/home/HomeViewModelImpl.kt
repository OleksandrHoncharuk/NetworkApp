package com.example.networkaplication.home

import android.app.Activity
import android.app.Application
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.networkaplication.R
import com.example.networkaplication.details.DetailsViewFragment
import com.example.networkaplication.home.adapter.HomeAdapter
import com.example.networkaplication.home.adapter.ItemData
import com.example.networkaplication.home.adapter.ListPresenter
import com.example.networkaplication.home.search.story.SearchItem
import com.example.networkaplication.home.search.story.SearchStoryAdapter
import com.example.networkaplication.home.search.story.SearchStoryPresenter
import com.example.networkaplication.idling.EspressoIdlingResource
import com.example.networkaplication.models.search.Search
import com.example.networkaplication.persistance.model.MovieQuery
import java.util.*

class HomeViewModelImpl internal constructor(application: Application, private val view: HomeContract.HomeView) : ViewModel(), HomeContract.HomeViewModel, HomeCallback {

    private val repository: HomeContract.HomeRepository
    private var searchItems = ArrayList<SearchItem>()
    private var adapter: HomeAdapter
    var searchAdapter: SearchStoryAdapter
    private var items = ArrayList<ItemData>()

    private val _searchText: MutableLiveData<String> = MutableLiveData()

    val searchText: LiveData<String> get() = _searchText

    init {
        _searchText.value = ""
        adapter = HomeAdapter(ListPresenter(items))
        adapter.setOnFilmClickedListener(this)

        searchAdapter = SearchStoryAdapter(SearchStoryPresenter(searchItems))
        searchAdapter.setOnSearchItemClickedListener(this)

        this.repository = HomeRepositoryImpl(application, this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard(v!!)
            clearSearchAdapter()
            search(v.text.toString())
        }
    }

    override fun setItemToDataAdapter() {
        adapter.refresh(items)
        view.setRecycleViewAdapter(adapter)
        adapter.notifyDataSetChanged()

        if (!EspressoIdlingResource.idlingResource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
    }

    override fun refreshSearchAdapter() {
        setSearchItemToAdapter()
    }

    override fun clearSearchAdapter() {
        searchItems = ArrayList()
        setSearchItemToAdapter()
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
            val movieQuery = MovieQuery(_searchText.value!!.toString(), System.currentTimeMillis())
            repository.insertIntoQuery(movieQuery)
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val isSearchPopped = HomeViewFragment.getIsSearchViewPopped()
        clearSearchAdapter()
        if (isSearchPopped) {
            val movies = ArrayList(repository.findAllFromQuery("$s%"))

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
                _searchText.value = itemData.searchText
                clearSearchAdapter()
            }

    override fun onClicked(itemData: ItemData) {
        val manager = view.getHomeViewActivity().supportFragmentManager

        manager
                .beginTransaction()
                .addToBackStack("Details")
                .replace(R.id.RelativeForFragments,
                        createDetailsView(itemData),
                        DetailsViewFragment::class.java.simpleName)
                .commit()
    }

    override fun onSearchReceived(search: Search) {
        val title = _searchText.value!!
        addUniqueMovie(title)


        val data = ArrayList<ItemData>()

        for (searchObject in search.search!!) {
            val item = ItemData(
                    searchObject.poster,
                    searchObject.title,
                    searchObject.imdbID)

            data.add(item)
        }

        items = data
        setItemToDataAdapter()
    }

    private fun setSearchItemToAdapter() {
        searchAdapter.refresh(searchItems)
        searchAdapter.notifyDataSetChanged()
    }

    override fun isSearchItemEmpty(): Boolean {
        return searchItems.size == 0
    }
}
