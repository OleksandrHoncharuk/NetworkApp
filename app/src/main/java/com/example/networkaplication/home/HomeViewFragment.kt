package com.example.networkaplication.home

import android.os.Bundle

import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.networkaplication.MainActivity
import com.example.networkaplication.R
import com.example.networkaplication.details.DetailsViewFragment
import com.example.networkaplication.home.adapter.HomeAdapter
import com.example.networkaplication.home.adapter.ItemData
import com.example.networkaplication.home.adapter.ListPresenter
import com.example.networkaplication.home.search.story.SearchItem
import com.example.networkaplication.home.search.story.SearchStoryAdapter
import com.example.networkaplication.home.search.story.SearchStoryPresenter
import com.example.networkaplication.idling.EspressoIdlingResource
import com.example.networkaplication.models.search.SearchObject

import java.util.ArrayList

class HomeViewFragment : Fragment(), HomeContract.HomeView {

    private var viewModel: HomeViewModelImpl? = null
    private var recyclerView: RecyclerView? = null
    private var searchItems = ArrayList<SearchItem>()
    private var searchRecycle: RecyclerView? = null
    private var searchAdapter: SearchStoryAdapter? = null
    private var search: EditText? = null

    override fun onStart() {
        super.onStart()
        activity!!.title = "Home Screen"
    }

    override fun onResume() {
        (activity as MainActivity)
                .supportActionBar!!
                .setDisplayHomeAsUpEnabled(false)
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel = ViewModelProviders.of(
                this,
                HomeViewModelFactory(activity!!.application, this))
                .get(HomeViewModelImpl::class.java)

        recyclerView = rootView.findViewById(R.id.home_recycle_view)
        recyclerView!!.layoutManager = GridLayoutManager(activity, 2)

        searchRecycle = rootView.findViewById(R.id.search_recycle_view)

        val manager = LinearLayoutManager(container!!.context)
        searchRecycle!!.layoutManager = manager
        searchRecycle!!.addItemDecoration(
                DividerItemDecoration(container.context, manager.orientation))
        searchAdapter = SearchStoryAdapter(SearchStoryPresenter(searchItems))
        searchAdapter!!.setOnSearchItemClickedListener(this)
        searchRecycle!!.adapter = searchAdapter
        searchRecycle!!.itemAnimator = DefaultItemAnimator()

        search = rootView.findViewById(R.id.search_button)
        search!!.setOnEditorActionListener(this)
        search!!.addTextChangedListener(this)
        search!!.onFocusChangeListener = this
        restoreSearchResult()

        return rootView
    }

    private fun restoreSearchResult() {
        viewModel!!.clearSearchAdapter()
        setItemDataToAdapter(viewModel!!.items)
//        if (arguments != null) {
//
//            val listImages = arguments!!.getStringArrayList("RESPONSE_IMAGES")
//            val listTitles = arguments!!.getStringArrayList("RESPONSE_TITLES")
//            val listIds = arguments!!.getStringArrayList("RESPONSE_IDS")
//            val data = ArrayList<ItemData>()
//
//            for (i in listImages!!.indices) {
//                data.add(ItemData(listImages[i],
//                        listTitles!![i],
//                        listIds!![i]))
//            }
//
//            setItemDataToAdapter(data)
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestFocus()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setRecycleViewAdapter(adapter: HomeAdapter) {
        recyclerView!!.adapter = adapter
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        adapter.notifyDataSetChanged()
    }

    override fun setSearchAdapter(item: ArrayList<SearchItem>) {
        this.searchItems = item
        searchAdapter!!.refresh(item)
        searchAdapter!!.notifyDataSetChanged()
    }

    override fun setBundleFromSearch(searchObjects: List<SearchObject>) {
        val result = Bundle()
        val images = ArrayList<String>()
        val titles = ArrayList<String>()
        val ids = ArrayList<String>()

        for (searchObject in searchObjects) {
            images.add(searchObject.poster!!)
            titles.add(searchObject.title!!)
            ids.add(searchObject.imdbID!!)
        }

        result.putStringArrayList("RESPONSE_IMAGES", images)
        result.putStringArrayList("RESPONSE_TITLES", titles)
        result.putStringArrayList("RESPONSE_IDS", ids)

        this.arguments = result
    }

    override fun setItemDataToAdapter(itemData: ArrayList<ItemData>) {
        val adapter = HomeAdapter(ListPresenter(viewModel!!.items))
        adapter.setOnFilmClickedListener(this)
        viewModel!!.setItemToDataAdapter(itemData)

        if (!EspressoIdlingResource.idlingResource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        Log.d("add", "add")
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        viewModel!!.onTextChanged(s, isSearchViewPopped)
    }

    override fun afterTextChanged(s: Editable) {
        Log.d("add", "add")
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        viewModel!!.onFocusChanged(v, hasFocus)
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        return viewModel!!.onEditorAction(v, actionId)
    }

    override fun getHomeViewActivity(): MainActivity {
        return activity as MainActivity
    }

    override fun getSearchRecycle(): RecyclerView {
        return searchRecycle!!
    }

    override fun getSearchText(): String {
        return search!!.text.toString()
    }

    override fun setSearchText(text: String) {
        search!!.setText(text)
    }

    override fun onClicked(itemData: ItemData) {
        val manager = activity!!.supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction
                .addToBackStack("Details")
                .replace(R.id.RelativeForFragments,
                        viewModel!!.createDetailsView(itemData),
                        DetailsViewFragment::class.java.simpleName)

        transaction.commit()

        (activity as MainActivity)
                .supportActionBar!!
                .setDisplayHomeAsUpEnabled(true)
    }

    override fun onClickedSearchItem(view: View, itemData: SearchItem) {
        viewModel!!.onClickedSearchItem(view.id, itemData)
    }

    companion object {
        private var isSearchViewPopped = true

        fun setIsSearchViewPopped(bool: Boolean) {
            isSearchViewPopped = bool
        }
    }
}
