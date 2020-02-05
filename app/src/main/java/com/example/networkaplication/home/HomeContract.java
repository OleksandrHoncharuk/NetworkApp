package com.example.networkaplication.home;

import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.example.networkaplication.MainActivity;
import com.example.networkaplication.details.DetailsViewFragment;
import com.example.networkaplication.home.adapter.HomeAdapter;
import com.example.networkaplication.home.adapter.ItemData;
import com.example.networkaplication.home.search.story.SearchItem;
import com.example.networkaplication.home.search.story.SearchStoryAdapter;
import com.example.networkaplication.models.search.SearchObject;
import com.example.networkaplication.persistance.model.MovieQuery;

import java.util.ArrayList;
import java.util.List;

public interface HomeContract {

    interface HomeView extends HomeAdapter.OnFilmClickedListener,
            SearchStoryAdapter.OnSearchItemClickedListener, TextView.OnEditorActionListener,
            TextWatcher, View.OnFocusChangeListener {

        MainActivity getHomeViewActivity();

        void setRecycleViewAdapter(HomeAdapter adapter);

        RecyclerView getSearchRecycle();

        void setSearchAdapter(ArrayList<SearchItem> item);

        String getSearchText();

        void setSearchText(String text);

        void setBundleFromSearch(List<SearchObject> objects);

        void setItemDataToAdapter(ArrayList<ItemData> itemData);
    }

    interface  HomePresenter {

        boolean onEditorAction(TextView v, int actionId);

        void setItemToDataAdapter(ArrayList<ItemData> items);

        void refreshSearchAdapter();

        DetailsViewFragment createDetailsView(ItemData itemData);

        void clearSearchAdapter();

        void hideKeyboard(View view);

        void search(String searchRequest);

        void addUniqueMovie(String title);

        void onTextChanged(CharSequence sequence, boolean isSearchPopped);

        void onFocusChanged(View view, boolean hasFocus);

        void onClickedSearchItem(int viewId, SearchItem itemData);
    }

    interface HomeRepository {
        void startSearch(String searchRequest);

        List<MovieQuery> findAllFromQuery(String serach);

        MovieQuery findByTitle(String title);

        void insertIntoQuery(MovieQuery query);
    }
}
