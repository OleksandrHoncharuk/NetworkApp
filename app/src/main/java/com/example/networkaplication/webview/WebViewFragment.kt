package com.example.networkaplication.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.example.networkaplication.R

import java.util.Objects

class WebViewFragment : Fragment() {

    var webView: WebView? = null
        private set
    private var url: String? = null

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        Objects.requireNonNull<FragmentActivity>(activity).setTitle("WebView")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null)
            url = arguments!!.getString(URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.web_view_fragment, container, false)

        webView = rootView.findViewById(R.id.web_view)
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = MyWebViewClient()
        webView!!.loadUrl(url)

        return rootView
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == android.R.id.home) {
            if (webView!!.canGoBack())
                webView!!.goBack()
            else
                Objects.requireNonNull<FragmentActivity>(activity).onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    fun setUrl(url: String) {
        this.url = url
    }

    companion object {
        private val URL = "URL"

        fun newInstance(url: String): WebViewFragment {
            val webViewFragment = WebViewFragment()
            val bundle = Bundle()
            bundle.putString(URL, url)
            webViewFragment.arguments = bundle

            return webViewFragment
        }
    }
}
