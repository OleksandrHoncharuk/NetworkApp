package com.example.networkaplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.example.networkaplication.MainActivity
import com.example.networkaplication.R
import com.example.networkaplication.databinding.FragmentHomeBinding
import com.example.networkaplication.details.DetailsViewFragment
import com.example.networkaplication.home.adapter.HomeAdapter
import com.example.networkaplication.home.adapter.ItemData
import kotlinx.android.synthetic.main.fragment_home.*

class HomeViewFragment : Fragment(), HomeContract.HomeView {

    private lateinit var viewModel: HomeViewModelImpl
    private lateinit var binding: FragmentHomeBinding

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestFocus()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(
                this,
                HomeViewModelFactory(activity!!.application, this))
                .get(HomeViewModelImpl::class.java)

        home_recycle_view.layoutManager = GridLayoutManager(activity, 2)

        val manager = LinearLayoutManager(context)

        search_recycle_view.layoutManager = manager
        search_recycle_view.addItemDecoration(
                DividerItemDecoration(context, manager.orientation))

        search_recycle_view.adapter = viewModel.searchAdapter
        search_recycle_view.itemAnimator = DefaultItemAnimator()

        binding.viewModel = viewModel

        viewModel.searchText.observe(this, Observer { value ->
            search_button.setText(value)})

        restoreSearchResult()
    }

    private fun restoreSearchResult() {
        viewModel.clearSearchAdapter()
        viewModel.setItemToDataAdapter()
    }

    override fun setRecycleViewAdapter(adapter: HomeAdapter) {
        binding.homeRecycleView.adapter = adapter
        binding.homeRecycleView.itemAnimator = DefaultItemAnimator()
    }

    override fun getHomeViewActivity(): MainActivity {
        return activity as MainActivity
    }

    override fun getSearchRecycle(): RecyclerView {
        return binding.searchRecycleView
    }

    companion object {
        private var isSearchViewPopped = true

        fun setIsSearchViewPopped(bool: Boolean) {
            isSearchViewPopped = bool
        }

        fun getIsSearchViewPopped(): Boolean {
            return isSearchViewPopped
        }
    }
}
