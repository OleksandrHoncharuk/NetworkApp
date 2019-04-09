package com.example.networkaplication.details;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.networkaplication.MainActivity;
import com.example.networkaplication.R;
import com.example.networkaplication.home.HomeFragment;
import com.example.networkaplication.models.select.MovieDetail;
import com.example.networkaplication.persistance.model.Details;
import com.example.networkaplication.persistance.repository.Repositories;
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl;
import com.example.networkaplication.persistance.repository.database.dao.DetailsDao;
import com.example.networkaplication.retrofit.NetworkService;
import com.example.networkaplication.webview.WebViewFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment implements View.OnClickListener {
    private String filmName;
    private ImageView poster;
    private TextView title;
    private TextView details;
    private TextView genre;
    private TextView plotSummary;
    private DetailsDao detailsDao;
    private SharedPreferences sharedPreferences;
    private static final String FILM_NAME = "FILM_NAME";
    private static final String OMDBID = "OMDBID";
    private String omdbId;
    private static File root = Environment.getExternalStorageDirectory();
    private boolean isOffline = false;

    public static DetailsFragment newInstance(String filmName, String omdbId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FILM_NAME, filmName);
        bundle.putString(OMDBID, omdbId);
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

        poster = rootView.findViewById(R.id.detail_poster);
        title = rootView.findViewById(R.id.film_name);
        details = rootView.findViewById(R.id.details);
        genre = rootView.findViewById(R.id.genre);
        plotSummary = rootView.findViewById(R.id.plot_summary);

        ImageButton save = rootView.findViewById(R.id.save_image);
        save.setOnClickListener(this);

        DatabaseRepositoryImpl db = (DatabaseRepositoryImpl)
                Repositories.getDatabase(Objects.requireNonNull(container).getContext());
        detailsDao = db.detailsDao();

//        detailsDao.deleteAll();
        sharedPreferences = ((MainActivity)getActivity()).getSharedPreferences("offline", Context.MODE_PRIVATE);

        boolean checked = false;
        if (sharedPreferences.contains(omdbId)) {
            checked = sharedPreferences.getBoolean(omdbId, false);
        }

        if (!checked) {
            NetworkService.getInstance()
                .getOMDBApi()
                .getMovieDetails(filmName)
                .enqueue(new MovieSearchCallback(this));


        } else {
            System.out.println("From Seving instance");

            if(detailsDao.findById(omdbId.trim()) != null) {
                setDetailsFragmentFrom(detailsDao.findById(omdbId.trim()));
                loadImageFromExStorage();
            }
        }

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

            case R.id.offline:
                saveIsOffline(item);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void backClicked() {
        HomeFragment.setSearchViewPopped(false);
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

    private void saveIsOffline(MenuItem item) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(omdbId, true);
            editor.apply();
        }

        isOffline = !isOffline;
        item.setChecked(isOffline);
    }

    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            saveImageToExStorage();
        }
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
                    String detailsLine = String
                            .format("%s %s %s %s/10 ", movie.getYear(), movie.getRuntime(),
                                    movie.getCountry(), movie.getImdbRating());

                    fragment.details.setText(detailsLine);

                    fragment.genre.setText(movie.getGenre());
                    fragment.plotSummary.setText(movie.getPlot());


                    Details details = fragment.detailsDao.findByTitle(movie.getTitle());
                    if (details == null) {
                        Details details1 = new Details(movie.getImdbID());
                        details1.setTitle(movie.getTitle());
                        details1.setGenre(movie.getGenre());
                        details1.setDetails(detailsLine);
                        details1.setPlotSummary(movie.getPlot());
                        fragment.detailsDao.insert(details1);
                    } else {
                        details.setTitle(movie.getTitle());
                        details.setDetails(detailsLine);
                        details.setGenre(movie.getGenre());
                        details.setPlotSummary(movie.getPlot());
                        fragment.detailsDao.update(details);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<MovieDetail> call, Throwable t) {
            if (fragmentWeakReference.get() != null) {
                DetailsFragment fragment = fragmentWeakReference.get();
                Details details = fragment.detailsDao.findById(fragment.omdbId);

                fragment.loadImageFromExStorage();
                fragment.setDetailsFragmentFrom(details);
            }

            call.cancel();
        }
    }

    private void setDetailsFragmentFrom(Details detail) {
        title.setText(detail.getTitle());
        details.setText(detail.getDetails());
        genre.setText(detail.getGenre());
        plotSummary.setText(detail.getPlotSummary());
    }

    public void saveImageToExStorage() {
        File file = new File(root, omdbId.trim() + ".PNG");
        Bitmap imageToSave = ((BitmapDrawable) poster.getDrawable()).getBitmap();

        try {
            FileOutputStream out = new FileOutputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            out.write(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromExStorage() {
        File file = new File(root, omdbId.trim() + ".PNG");
        if (file.exists()) {
            poster.setImageURI(Uri.fromFile(file));
        }
    }
}
