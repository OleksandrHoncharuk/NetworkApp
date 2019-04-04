package com.example.networkaplication.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.networkaplication.details.DetailsFragment;
import com.example.networkaplication.MainActivity;
import com.example.networkaplication.R;
import com.example.networkaplication.models.search.Search;
import com.example.networkaplication.models.search.SearchObject;
import com.example.networkaplication.retrofit.NetworkService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeAdapter.OnFilmClickedListener {

    private RecyclerView recyclerView;
    private ArrayList<ItemData> itemData = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).setTitle("Home Screen");
    }

    @Override
    public void onResume() {
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.home_recycle_view);
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(grid);

        final EditText search = rootView.findViewById(R.id.search_button);
//        search.setFocusable(true);
//        search.setClickable(true);
//        search.setCursorVisible(true);

        if (getArguments() != null) {
            ArrayList<String> listImages = getArguments().getStringArrayList("RESPONSE_IMAGES");
            ArrayList<String> listTitles = getArguments().getStringArrayList("RESPONSE_TITLES");

            ArrayList<ItemData> data = new ArrayList<>();

            for (int i = 0; i < Objects.requireNonNull(listImages).size(); i++) {
                data.add(new ItemData(listImages.get(i), Objects.requireNonNull(listTitles).get(i)));
            }

            itemData = data;
            setItemDataToAdapter();
        }

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean result = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(v.getText().toString());
                    result = true;
                }
                return result;
            }
        });

        return rootView;
    }


    private void search(String searchRequest) {
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

    @Override
    public void onClicked(ItemData itemData) {
        DetailsFragment fragment = DetailsFragment.newInstance(itemData.getTitle());
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("").replace(R.id.RelativeForFragments, fragment);
        transaction.commit();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
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
                final Bundle result = new Bundle();
                final ArrayList<String> images = new ArrayList<>();
                final ArrayList<String> titles = new ArrayList<>();

                Search search = response.body();

                if (Objects.requireNonNull(search).getSearch() != null && search.getSearch().size() != 0) {
                    ArrayList<ItemData> data = new ArrayList<>();

                    for (SearchObject object : search.getSearch()) {
                        ItemData item = new ItemData(object.getPoster(), object.getTitle());

                        images.add(object.getPoster());
                        titles.add(object.getTitle());
                        data.add(item);
                    }

                    home.itemData = data;
                    result.putStringArrayList("RESPONSE_IMAGES", images);
                    result.putStringArrayList("RESPONSE_TITLES", titles);
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
}
