package com.example.networkaplication.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.networkaplication.MainActivity;
import com.example.networkaplication.R;
import com.example.networkaplication.home.adapter.HomeAdapter;
import com.example.networkaplication.home.adapter.ItemData;
import com.example.networkaplication.home.adapter.ListPresenter;
import com.example.networkaplication.home.search.story.SearchItem;
import com.example.networkaplication.home.search.story.SearchStoryAdapter;
import com.example.networkaplication.home.search.story.SearchStoryPresenter;
import com.example.networkaplication.models.search.Search;
import com.example.networkaplication.models.search.SearchObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewFragment extends Fragment implements HomeContract.HomeView {

    private HomePresenterImpl presenter;
    private RecyclerView recyclerView;
    private ArrayList<SearchItem> searchItems = new ArrayList<>();
    private RecyclerView searchRecycleView;
    private SearchStoryAdapter searchAdapter;
    private EditText search;
    private static boolean isSearchViewPopped = true;

    public HomeViewFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Home Screen");
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity())
                .getSupportActionBar()
                .setDisplayHomeAsUpEnabled(false);
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomePresenterImpl(getActivity().getApplication(), this);

        recyclerView = rootView.findViewById(R.id.home_recycle_view);
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(grid);

//        movieQueryDao.deleteAll();

        searchRecycleView = rootView.findViewById(R.id.search_recycle_view);

        LinearLayoutManager manager = new LinearLayoutManager(container.getContext());
        searchRecycleView.setLayoutManager(manager);
        searchRecycleView.addItemDecoration(
                new DividerItemDecoration(container.getContext(), manager.getOrientation()));
        searchAdapter = new SearchStoryAdapter(new SearchStoryPresenter(searchItems));
        searchAdapter.setOnSearchItemClickedListener(this);
        searchRecycleView.setAdapter(searchAdapter);
        searchRecycleView.setItemAnimator(new DefaultItemAnimator());

        search = rootView.findViewById(R.id.search_button);
        search.setOnEditorActionListener(this);
        search.addTextChangedListener(this);
        search.setOnFocusChangeListener(this);
        restoreSearchResult();

        return rootView;
    }

    private void restoreSearchResult () {
        Log.d("RESTORE", "before if");

        presenter.refreshSearchAdapter(new ArrayList<SearchItem>());
        if (getArguments() != null) {

            ArrayList<String> listImages = getArguments().getStringArrayList("RESPONSE_IMAGES");
            ArrayList<String> listTitles = getArguments().getStringArrayList("RESPONSE_TITLES");
            ArrayList<String> listIds = getArguments().getStringArrayList("RESPONSE_IDS");
            Log.d("Inside if", listIds.size() + "");
            ArrayList<ItemData> data = new ArrayList<>();

            for (int i = 0; i < Objects.requireNonNull(listImages).size(); i++) {
                data.add(new ItemData(listImages.get(i),
                        listTitles.get(i),
                        listIds.get(i)));
            }

            setItemDataToAdapter(data);
        }
        Log.d("RESTORE", "after if");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.requestFocus();
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public MainActivity getHomeViewActivity() {
        return (MainActivity)getActivity();
    }

    @Override
    public RecyclerView getFragmentRecycle() {
        return this.recyclerView;
    }

    @Override
    public void setRecycleViewAdapter(HomeAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public RecyclerView getSearchRecycle() {
        return this.searchRecycleView;
    }

    @Override
    public void setItemData(ArrayList<ItemData> itemData) {
        presenter.setItemToDataAdapter(itemData);
    }

    @Override
    public void setSearchAdapter(ArrayList<SearchItem> item) {
        searchAdapter.refresh(item);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public String getSearchText() {
        return this.search.getText().toString();
    }

    @Override
    public void setSearchText(String text) {
        search.setText(text);
    }

    @Override
    public void setArgumentsToView(Bundle bundle) {
        this.setArguments(bundle);
    }

    @Override
    public void setItemDataToAdapter(ArrayList<ItemData> data) {
        HomeAdapter adapter = new HomeAdapter(new ListPresenter(data));
        adapter.setOnFilmClickedListener(this);
        presenter.setItemToDataAdapter(data);
    }

    @Override
    public void addMovieIfNotExist(String title) {
        presenter.addUniqueMovie(title);
    }

    @Override
    public boolean getIsSearchViewPopped() {
        return isSearchViewPopped;
    }

    public static void setIsSearchViewPopped(boolean bool) {
        isSearchViewPopped = bool;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("add", "add");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onTextChanged(s, isSearchViewPopped);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("add", "add");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        presenter.onFocusChanged(v, hasFocus);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return presenter.onEditorAction(v, actionId);
    }

    @Override
    public void onClicked(ItemData itemData) {
        presenter.startDetailsView(itemData);
    }

    @Override
    public void onClickedSearchItem(View view, SearchItem itemData) {
        presenter.onClickedSearchItem(view, itemData);
    }


    public static class SearchCallback implements Callback<Search> {
        private final WeakReference<HomeContract.HomeView> homeWeakReference;

        SearchCallback(HomeContract.HomeView view) {
            homeWeakReference = new WeakReference<>(view);
        }

        @Override
        public void onResponse(Call<Search> call, Response<Search> response) {
            if (homeWeakReference.get() != null) {
                HomeContract.HomeView home = homeWeakReference.get();
                home.setItemData(new ArrayList<ItemData>());

                final Bundle result = new Bundle();
                final ArrayList<String> images = new ArrayList<>();
                final ArrayList<String> titles = new ArrayList<>();
                final ArrayList<String> ids = new ArrayList<>();

                Search search = response.body();

                if (search.getSearch() != null && search.getSearch().size() != 0) {

                    String title = home.getSearchText();
                    home.addMovieIfNotExist(title);

                    ArrayList<ItemData> data = new ArrayList<>();

                    for (SearchObject object : search.getSearch()) {
                        ItemData item = new ItemData(
                                object.getPoster(),
                                object.getTitle(),
                                object.getImdbID());

                        images.add(object.getPoster());
                        titles.add(object.getTitle());
                        ids.add(object.getImdbID());
                        data.add(item);
                        Log.d("Item", item.getTitle());
                    }

                    result.putStringArrayList("RESPONSE_IMAGES", images);
                    result.putStringArrayList("RESPONSE_TITLES", titles);
                    result.putStringArrayList("RESPONSE_IDS", ids);
                    home.setArgumentsToView(result);

                    Log.d("Not attached adapter", (home.getFragmentRecycle().getAdapter().getClass()) + "");
                    home.setItemDataToAdapter(data);
                }
            }
        }

        @Override
        public void onFailure(Call<Search> call, Throwable t) {
            call.cancel();
        }
    }
}
