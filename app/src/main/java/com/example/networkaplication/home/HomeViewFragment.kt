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
import androidx.recyclerview.widget.*
import com.example.networkaplication.MainActivity
import com.example.networkaplication.R
import com.example.networkaplication.details.DetailsViewFragment
import com.example.networkaplication.home.adapter.HomeAdapter
import com.example.networkaplication.home.adapter.ItemData
import com.example.networkaplication.home.search.story.SearchItem
import com.example.networkaplication.home.search.story.SearchStoryAdapter
import com.example.networkaplication.home.search.story.SearchStoryPresenter
import java.util.*

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

        recyclerView = rootView.findViewById(R.id.home_recycle_view)
        recyclerView!!.layoutManager = GridLayoutManager(activity, 2)
        searchRecycle = rootView.findViewById(R.id.search_recycle_view)
        search = rootView.findViewById(R.id.search_button)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(
                this,
                HomeViewModelFactory(activity!!.application, this))
                .get(HomeViewModelImpl::class.java)

        val manager = LinearLayoutManager(context)
        searchRecycle!!.layoutManager = manager
        searchRecycle!!.addItemDecoration(
                DividerItemDecoration(context, manager.orientation))

        searchRecycle!!.adapter = viewModel!!.searchAdapter
        searchRecycle!!.itemAnimator = DefaultItemAnimator()

        search!!.setOnEditorActionListener(this)
        search!!.addTextChangedListener(this)
        search!!.onFocusChangeListener = this
        restoreSearchResult()
    }

    private fun restoreSearchResult() {
        viewModel!!.clearSearchAdapter()
        viewModel!!.setItemToDataAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestFocus()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setRecycleViewAdapter(adapter: HomeAdapter) {
        recyclerView!!.adapter = adapter
        recyclerView!!.itemAnimator = DefaultItemAnimator()
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

        manager
                .beginTransaction()
                .addToBackStack("Details")
                .replace(R.id.RelativeForFragments,
                        viewModel!!.createDetailsView(itemData),
                        DetailsViewFragment::class.java.simpleName)
                .commit()


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
