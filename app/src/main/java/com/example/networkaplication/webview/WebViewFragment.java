package com.example.networkaplication.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.networkaplication.R;

import java.util.Objects;

public class WebViewFragment extends Fragment {

    private WebView webView;
    private String url;
    private static final String URL = "URL";

    public static WebViewFragment newInstance(String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        webViewFragment.setArguments(bundle);

        return webViewFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        Objects.requireNonNull(getActivity()).setTitle("WebView");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            url = getArguments().getString(URL);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.web_view_fragment, container, false);

        webView = rootView.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView.canGoBack())
                    webView.goBack();
                else
                    Objects.requireNonNull(getActivity()).onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebView getWebView() {
        return webView;
    }
}
