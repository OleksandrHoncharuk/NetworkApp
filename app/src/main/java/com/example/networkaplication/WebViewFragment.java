package com.example.networkaplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewFragment extends Fragment {

    private WebView webView;
    private String url;

    public static WebViewFragment newInstance (String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.setUrl(url);

        return webViewFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        getActivity().setTitle("WebView");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                    getActivity().onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public WebView getWebView () {
        return webView;
    }
}
