package com.example.networkaplication.details;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.networkaplication.R;
import com.example.networkaplication.models.select.MovieDetail;
import com.example.networkaplication.retrofit.NetworkService;
import com.example.networkaplication.webview.WebViewFragment;

import java.lang.ref.WeakReference;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {
    private String filmName;
    private ImageView poster;
    private TextView title;
    private TextView details;
    private TextView genre;
    private TextView plotSummary;
    private static final String FILM_NAME = "FILM_NAME";

    public static DetailsFragment newInstance(String filmName) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FILM_NAME, filmName);
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
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.details_fragment, container, false);

        poster = rootView.findViewById(R.id.detail_poster);
        title = rootView.findViewById(R.id.film_name);
        details = rootView.findViewById(R.id.details);
        genre = rootView.findViewById(R.id.genre);
        plotSummary = rootView.findViewById(R.id.plot_summary);

        NetworkService.getInstance()
                .getOMDBApi()
                .getMovieDetails(filmName)
                .enqueue(new MovieSearchCallback(this));

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
        switch (item.getItemId()) {
            case android.R.id.home:
                backClicked();
                return true;

            case R.id.web_view_item:
                showWebView();
                return true;

            case R.id.browser:
                showBrowser();
                return true;

            case R.id.alert_dialog:
                showAlertDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void backClicked() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    private void showWebView() {
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("").replace(R.id.RelativeForFragments,
                WebViewFragment.newInstance(NetworkService.BASE_URL),
                WebViewFragment.class.getSimpleName());
        transaction.commit();
    }

    private void showBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkService.BASE_URL));
        startActivity(browserIntent);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(Objects.requireNonNull(this.getContext()));

        builder.setTitle("Very dump message!")
                .setMessage("This...is..ALERT DIALOG!")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alert = builder.create();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alert.show();
            }
        }, 5000);
    }

    @SuppressWarnings("NullableProblems")
    private static class MovieSearchCallback implements Callback<MovieDetail> {
        private final WeakReference<DetailsFragment> fragmentWeakReference;

        MovieSearchCallback(DetailsFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
            if (fragmentWeakReference.get() != null) {
                DetailsFragment fragment = fragmentWeakReference.get();
                MovieDetail movie = response.body();

                if (movie != null) {
                    Glide
                            .with(Objects.requireNonNull(fragment.getContext()))
                            .load(movie.getPoster())
                            .into(fragment.poster);

                    fragment.title.setText(movie.getTitle());
                    fragment.details.setText(String
                            .format("%s %s %s %s/10 ", movie.getYear(), movie.getRuntime(),
                                    movie.getCountry(), movie.getImdbRating()));

                    fragment.genre.setText(movie.getGenre());
                    fragment.plotSummary.setText(movie.getPlot());
                }
            }
        }

        @Override
        public void onFailure(Call<MovieDetail> call, Throwable t) {
            call.cancel();
        }
    }
}
