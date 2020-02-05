package com.example.networkaplication.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.networkaplication.MainActivity;
import com.example.networkaplication.R;
import com.example.networkaplication.persistance.model.Details;

import java.util.Objects;

public class DetailsViewFragment extends Fragment implements DetailsContract.DetailsView,
        View.OnClickListener {

    private String filmName;
    private ImageView poster;
    private TextView title;
    private TextView details;
    private TextView genre;
    private TextView plotSummary;
    private SharedPreferences sharedPreferences;
    private static final String FILM_NAME = "FILM_NAME";
    private static final String OMDBID = "OMDBID";
    private String omdbId;
    private boolean isOffline = false;
    private DetailsContract.DetailsPresenter presenter;

    public static DetailsViewFragment newInstance(String filmName, String omdbId) {
        DetailsViewFragment fragment = new DetailsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FILM_NAME, filmName);
        bundle.putString(OMDBID, omdbId.trim());
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        if (getArguments() != null) {
            filmName = getArguments().getString(FILM_NAME);
            omdbId = getArguments().getString(OMDBID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.details_fragment, container, false);

        presenter = new DetailsPresenterImpl(getActivity().getApplication(), this);
        poster = rootView.findViewById(R.id.detail_poster);
        title = rootView.findViewById(R.id.film_name);
        details = rootView.findViewById(R.id.details);
        genre = rootView.findViewById(R.id.genre);
        plotSummary = rootView.findViewById(R.id.plot_summary);

        ImageButton save = rootView.findViewById(R.id.save_image);
        save.setOnClickListener(this);

        sharedPreferences = ((MainActivity) getActivity())
                .getSharedPreferences("offline", Context.MODE_PRIVATE);

        boolean checked = false;

        if (sharedPreferences.contains(omdbId)) {
            checked = sharedPreferences.getBoolean(omdbId, false);
        }

        if (!checked)
            presenter.initView(filmName);
        else
            presenter.initViewById(omdbId);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).setTitle("Details");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (presenter.onOptionItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        presenter.onSaveImageClicked(omdbId);
    }

    @Override
    public Bitmap getImage() {
        return ((BitmapDrawable) poster.getDrawable()).getBitmap();
    }

    @Override
    public void setImage(Uri imageUri) {
        poster.setImageURI(imageUri);
    }

    @Override
    public void initDetails(Details detail) {
        title.setText(detail.getTitle());
        details.setText(detail.getDetails());
        genre.setText(detail.getGenre());
        plotSummary.setText(detail.getPlotSummary());
    }

    @Override
    public ImageView getImageView() {
        return poster;
    }

    @Override
    public MainActivity getViewActivity() {
        return (MainActivity)getActivity();
    }

    public void saveImageToExStorage(){
        presenter.onSaveImageClicked(omdbId);
    }
}
