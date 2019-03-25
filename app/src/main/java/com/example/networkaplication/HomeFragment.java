package com.example.networkaplication;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.networkaplication.pojo.item.search.Search;
import com.example.networkaplication.pojo.item.search.SearchObject;
import com.example.networkaplication.retrofit.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeAdapter.OnFilmClicked {

    private RecyclerView recyclerView;
    private ArrayList<ItemData> itemData = new ArrayList<>();

    public HomeFragment () {}

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Home Screen");
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
        search.setFocusable(true);
        search.setClickable(true);
        search.setCursorVisible(true);

        if (getArguments() != null) {
            ArrayList<String> listImages = getArguments().getStringArrayList("RESPONSE_IMAGES");
            ArrayList<String> listTitles = getArguments().getStringArrayList("RESPONSE_TITLES");

            ArrayList<ItemData> data = new ArrayList<>();

            for (int i = 0; i < listImages.size(); i++) {
                data.add(new ItemData(listImages.get(i), listTitles.get(i)));
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


    private void search (String searchRequest) {

        final HomeFragment home = this;

        final Bundle result = new Bundle();
        final ArrayList<String> images = new ArrayList<>();
        final ArrayList<String> titles = new ArrayList<>();

        NetworkService.getInstance()
                .getOMDBApi()
                .getSearchResult(searchRequest)
                .enqueue(new Callback<Search>() {
                    @Override
                    public void onResponse(Call<Search> call, Response<Search> response) {
                        Search search = response.body();

                        if (search.getSearch().size() != 0) {
                            ArrayList<ItemData> data = new ArrayList<>();

                            for (SearchObject object : search.getSearch()) {
                                ItemData item = new ItemData(object.getPoster(), object.getTitle());

                                images.add(object.getPoster());
                                titles.add(object.getTitle());
                                data.add(item);
                            }

                            itemData = data;
                            result.putStringArrayList("RESPONSE_IMAGES", images);
                            result.putStringArrayList("RESPONSE_TITLES", titles);
                            home.setArguments(result);

                            setItemDataToAdapter();
                        }


                    }

                    @Override
                    public void onFailure(Call<Search> call, Throwable t) {
                        call.cancel();
                    }
                });
    }

    private void setItemDataToAdapter () {
        HomeAdapter adapter = new HomeAdapter(itemData);
        adapter.setOnFilmClicked(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(ItemData itemData) {
        DetailsFragment fragment = DetailsFragment.newInstance(itemData.getTitle());
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("").replace(R.id.RelativeForFragments, fragment);
        transaction.commit();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
