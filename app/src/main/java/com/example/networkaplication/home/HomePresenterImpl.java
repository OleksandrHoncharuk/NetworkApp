package com.example.networkaplication.home;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.networkaplication.R;
import com.example.networkaplication.details.DetailsViewFragment;
import com.example.networkaplication.home.adapter.HomeAdapter;
import com.example.networkaplication.home.adapter.ItemData;
import com.example.networkaplication.home.adapter.ListPresenter;
import com.example.networkaplication.home.search.story.SearchItem;
import com.example.networkaplication.persistance.model.MovieQuery;

import java.util.ArrayList;

public class HomePresenterImpl extends AndroidViewModel implements HomeContract.HomePresenter {

    private HomeContract.HomeView view;
    private HomeContract.HomeReposytory repository;
    private ArrayList<SearchItem> searchItems = new ArrayList<>();

    public HomePresenterImpl(@NonNull Application application, HomeContract.HomeView view) {
        super(application);
        this.view = view;
        this.repository = new HomeRepositoryImpl(application);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId) {
        boolean result = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard(v);
            refreshSearchAdapter(new ArrayList<SearchItem>());
            search(v.getText().toString());
            result = true;
        }
        return result;
    }

    @Override
    public void setItemToDataAdapter(ArrayList<ItemData> items) {
        HomeAdapter adapter = new HomeAdapter(new ListPresenter(items));
        adapter.setOnFilmClickedListener(view);
        view.setRecycleViewAdapter(adapter);
    }

    @Override
    public void refreshSearchAdapter(ArrayList<SearchItem> item) {
        if (item.size() == 0)
            view.getSearchRecycle().setVisibility(View.INVISIBLE);
        else
            view.getSearchRecycle().setVisibility(View.VISIBLE);

        view.setSearchAdapter(item);
    }

    @Override
    public void startDetailsView(ItemData itemData) {
        DetailsViewFragment view = DetailsViewFragment.newInstance(itemData.getTitle(), itemData.getOmdbId());
        FragmentManager manager = this.view.getHomeViewActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(DetailsViewFragment.class.getSimpleName())
                .replace(R.id.RelativeForFragments, view);
        transaction.commit();
        this.view.getHomeViewActivity()
                .getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setTextToView() {

    }

    @Override
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.view.getHomeViewActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void search(String searchRequest) {
        repository.startSearch(searchRequest, view);
    }

    @Override
    public void addUniqueMovie(String title) {

        if (repository.findByTitle(title) == null) {
            MovieQuery movieQuery = new MovieQuery(view.getSearchText(),
                    System.currentTimeMillis());
            repository.insertIntoQuery(movieQuery);
        }
    }

    @Override
    public void onTextChanged(CharSequence sequence, boolean isSearchPopped) {
        refreshSearchAdapter(new ArrayList<SearchItem>());
        if (isSearchPopped) {
            ArrayList<MovieQuery> movies = new ArrayList<>(
                    repository.findAllFromQuery(sequence.toString() + "%"));

            for (MovieQuery movie : movies) {
                searchItems.add(new SearchItem(movie.getName()));
            }

            refreshSearchAdapter(searchItems);
        } else HomeViewFragment.setIsSearchViewPopped(true);
    }

    @Override
    public void onFocusChanged(View view, boolean hasFocus) {
        if (hasFocus) {
            refreshSearchAdapter(searchItems);
        } else {
            refreshSearchAdapter(new ArrayList<SearchItem>());
            hideKeyboard(view);
        }
    }

    @Override
    public void onClickedSearchItem(View view, SearchItem itemData) {
        if (view.getId() == R.id.delete_query) {
            searchItems.remove(itemData);
            refreshSearchAdapter(searchItems);

        }
        else {
            this.view.setSearchText(itemData.getSearchText());
            refreshSearchAdapter(new ArrayList<SearchItem>());
        }
    }
}
