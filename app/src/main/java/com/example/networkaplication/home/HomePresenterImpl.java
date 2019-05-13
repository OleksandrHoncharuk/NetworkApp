package com.example.networkaplication.home;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
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
import com.example.networkaplication.models.search.Search;
import com.example.networkaplication.models.search.SearchObject;
import com.example.networkaplication.persistance.model.MovieQuery;

import java.util.ArrayList;

public class HomePresenterImpl extends AndroidViewModel implements HomeContract.HomePresenter, HomeCallback {

    private HomeContract.HomeView view;
    private HomeContract.HomeRepository repository;
    private ArrayList<SearchItem> searchItems = new ArrayList<>();

    HomePresenterImpl(@NonNull Application application, HomeContract.HomeView view) {
        super(application);
        this.view = view;
        this.repository = new HomeRepositoryImpl(application, this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId) {
        boolean result = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard(v);
            clearSearchAdapter();
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
    public void refreshSearchAdapter() {
        if (searchItems.size() == 0)
            view.getSearchRecycle().setVisibility(View.INVISIBLE);
        else
            view.getSearchRecycle().setVisibility(View.VISIBLE);

        view.setSearchAdapter(searchItems);
    }

    @Override
    public void clearSearchAdapter() {
        view.getSearchRecycle().setVisibility(View.INVISIBLE);
        searchItems = new ArrayList<>();
        view.setSearchAdapter(searchItems);
    }

    @Override
    public DetailsViewFragment createDetailsView(ItemData itemData) {
        return DetailsViewFragment.newInstance(itemData.getTitle(), itemData.getOmdbId());
    }

    @Override
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.view.getHomeViewActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void search(String searchRequest) {
        repository.startSearch(searchRequest);
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
        clearSearchAdapter();
        if (isSearchPopped) {
            ArrayList<MovieQuery> movies = new ArrayList<>(
                    repository.findAllFromQuery(sequence.toString() + "%"));

            for (MovieQuery movie : movies) {
                searchItems.add(new SearchItem(movie.getName()));
            }

            refreshSearchAdapter();
        } else HomeViewFragment.setIsSearchViewPopped(true);
    }

    @Override
    public void onFocusChanged(View view, boolean hasFocus) {
        if (hasFocus) {
            refreshSearchAdapter();
        } else {
            clearSearchAdapter();
            hideKeyboard(view);
        }
    }

    @Override
    public void onClickedSearchItem(int viewId, SearchItem itemData) {
        if (viewId == R.id.delete_query) {
            searchItems.remove(itemData);
            refreshSearchAdapter();
        } else {
            view.setSearchText(itemData.getSearchText());
            clearSearchAdapter();
        }
    }

    @Override
    public void onSearchReceived(Search search) {
        String title = view.getSearchText();
        addUniqueMovie(title);

        ArrayList<ItemData> data = new ArrayList<>();

        for (SearchObject object : search.getSearch()) {
            ItemData item = new ItemData(
                    object.getPoster(),
                    object.getTitle(),
                    object.getImdbID());

            data.add(item);
        }

        view.setBundleFromSearch(search.getSearch());
        view.setItemDataToAdapter(data);
    }
}
