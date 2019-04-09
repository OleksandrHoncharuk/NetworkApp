package com.example.networkaplication.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.networkaplication.details.DetailsFragment;
import com.example.networkaplication.MainActivity;
import com.example.networkaplication.R;
import com.example.networkaplication.models.search.Search;
import com.example.networkaplication.models.search.SearchObject;
import com.example.networkaplication.persistance.model.MovieQuery;
import com.example.networkaplication.persistance.repository.Repositories;
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl;
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao;
import com.example.networkaplication.retrofit.NetworkService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeAdapter.OnFilmClickedListener,
        SearchStoryAdapter.OnSearchItemClickedListener, TextView.OnEditorActionListener, TextWatcher,
        View.OnFocusChangeListener {

    private RecyclerView recyclerView;
    private ArrayList<ItemData> itemData = new ArrayList<>();
    private ArrayList<SearchItem> searchItems = new ArrayList<>();
    private MovieQueryDao movieQueryDao;
    private RecyclerView searchRecycleView;
    private SearchStoryAdapter searchAdapter;
    private HomeFragment home = this;
    private EditText search;
    private static boolean isSearchViewPopped = true;

    public HomeFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).setTitle("Home Screen");
    }

    @Override
    public void onResume() {
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar())
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
        recyclerView = rootView.findViewById(R.id.home_recycle_view);
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(grid);

        DatabaseRepositoryImpl db = (DatabaseRepositoryImpl) Repositories.getDatabase(home.getContext());
        movieQueryDao = db.movieQueryDao();

//        movieQueryDao.deleteAll();

        searchRecycleView = rootView.findViewById(R.id.search_recycle_view);

        LinearLayoutManager manager = new LinearLayoutManager(container.getContext());
        searchRecycleView.setLayoutManager(manager);
        searchRecycleView.addItemDecoration(
                new DividerItemDecoration(container.getContext(), manager.getOrientation()));
        searchAdapter = new SearchStoryAdapter(searchItems);
        searchAdapter.setOnSearchItemClickedListener(home);
        searchRecycleView.setAdapter(searchAdapter);
        searchRecycleView.setItemAnimator(new DefaultItemAnimator());

        search = rootView.findViewById(R.id.search_button);
        search.setOnEditorActionListener(this);
        search.addTextChangedListener(this);
        search.setOnFocusChangeListener(this);
        restoreSearchResult();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.requestFocus();
        super.onViewCreated(view, savedInstanceState);
    }

    public EditText getSearch() {
        return search;
    }

    private void restoreSearchResult () {
        Log.d("RESTORE", "before if");

        refreshSearchAdapter(new ArrayList<SearchItem>());
        if (getArguments() != null) {
            ArrayList<String> listImages = getArguments().getStringArrayList("RESPONSE_IMAGES");
            ArrayList<String> listTitles = getArguments().getStringArrayList("RESPONSE_TITLES");
            ArrayList<String> listIds = getArguments().getStringArrayList("RESPONSE_IDS");

            ArrayList<ItemData> data = new ArrayList<>();

            for (int i = 0; i < Objects.requireNonNull(listImages).size(); i++) {
                data.add(new ItemData(listImages.get(i),
                        Objects.requireNonNull(listTitles).get(i),
                        Objects.requireNonNull(listIds).get(i)));
            }

            itemData = data;
            setItemDataToAdapter();
        }
        Log.d("RESTORE", "after if");
    }

    private void search(String searchRequest) {
        Log.d("NETWORK_SEARCH", "network search");
        NetworkService.getInstance()
                .getOMDBApi()
                .getSearchResult(searchRequest)
                .enqueue(new SearchCallback(this));
    }

    private void setItemDataToAdapter() {
        HomeAdapter adapter = new HomeAdapter(itemData);
        adapter.setOnFilmClickedListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    private void refreshSearchAdapter(ArrayList<SearchItem> item) {
        if (item.size() == 0)
            searchRecycleView.setVisibility(View.INVISIBLE);
         else
            searchRecycleView.setVisibility(View.VISIBLE);

        searchItems = item;
        searchAdapter.refresh(searchItems);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(ItemData itemData) {
        DetailsFragment fragment = DetailsFragment.newInstance(itemData.getTitle(), itemData.getOmdbId());
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(DetailsFragment.class.getSimpleName())
                .replace(R.id.RelativeForFragments, fragment);
        transaction.commit();
        Objects.requireNonNull(((MainActivity) getActivity())
                .getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClickedSearchItem(View view, SearchItem itemData) {
        if (view.getId() == R.id.delete_query) {
            searchItems.remove(itemData);
            refreshSearchAdapter(searchItems);
        }
        else {
            getSearch().setText(itemData.getSearchText());
            refreshSearchAdapter(new ArrayList<SearchItem>());
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d("EDITOR_ACTION", "editor search before if");
        boolean result = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Log.d("EDITOR_ACTION", "editor search inside if");
            hideKeyboardFromView(v);
            refreshSearchAdapter(new ArrayList<SearchItem>());
            search(v.getText().toString());
            result = true;
        }
        return result;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("BEFORE_TEXT", "before text changed");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        refreshSearchAdapter(new ArrayList<SearchItem>());
        if (isSearchViewPopped) {
            ArrayList<MovieQuery> movies = new ArrayList<>(movieQueryDao
                    .findAllLike(s.toString() + "%"));

            for (MovieQuery movie : movies) {
                searchItems.add(new SearchItem(movie.getName()));
            }

            refreshSearchAdapter(searchItems);
        } else isSearchViewPopped = !isSearchViewPopped;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            refreshSearchAdapter(searchItems);
        } else {
            refreshSearchAdapter(new ArrayList<SearchItem>());
            hideKeyboardFromView(v);
        }
    }

    @SuppressWarnings("NullableProblems")
    private static class SearchCallback implements Callback<Search> {
        private final WeakReference<HomeFragment> homeWeakReference;

        SearchCallback(HomeFragment fragment) {
            homeWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onResponse(Call<Search> call, Response<Search> response) {
            if (homeWeakReference.get() != null) {
                HomeFragment home = homeWeakReference.get();
                home.itemData = new ArrayList<>();

                final Bundle result = new Bundle();
                final ArrayList<String> images = new ArrayList<>();
                final ArrayList<String> titles = new ArrayList<>();
                final ArrayList<String> ids = new ArrayList<>();

                Search search = response.body();

                if (Objects.requireNonNull(search).getSearch() != null && search.getSearch().size() != 0) {

                    String title = home.getSearch().getText().toString();

                    if (home.movieQueryDao.findByTitle(title) == null) {
                        MovieQuery movieQuery = new MovieQuery(home.getSearch().getText().toString(),
                                System.currentTimeMillis());
                        home.movieQueryDao.insert(movieQuery);
                    }

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
                    }

                    home.itemData = data;
                    result.putStringArrayList("RESPONSE_IMAGES", images);
                    result.putStringArrayList("RESPONSE_TITLES", titles);
                    result.putStringArrayList("RESPONSE_IDS", ids);
                    home.setArguments(result);

                    home.setItemDataToAdapter();
                }
            }
        }

        @Override
        public void onFailure(Call<Search> call, Throwable t) {
            call.cancel();
        }
    }

    public void hideKeyboardFromView (View view) {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setSearchViewPopped(boolean searchViewPopped) {
        isSearchViewPopped = searchViewPopped;
    }
}
