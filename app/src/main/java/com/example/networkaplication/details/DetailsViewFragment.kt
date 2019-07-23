package com.example.networkaplication.details

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

import com.example.networkaplication.MainActivity
import com.example.networkaplication.R
import com.example.networkaplication.home.HomeViewFragment
import com.example.networkaplication.idling.EspressoIdlingResource
import com.example.networkaplication.persistance.model.Details
import com.example.networkaplication.retrofit.RetrofitModule
import com.example.networkaplication.webview.WebViewFragment

class DetailsViewFragment : Fragment(), DetailsContract.DetailsView, View.OnClickListener {

    private lateinit var filmName: String
    override lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var genre: TextView
    private lateinit var plotSummary: TextView
    private lateinit var save: ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var omdbId: String
    private val isOffline = false
    private lateinit var offline: MenuItem
    private lateinit var viewModel: DetailsContract.DetailsViewModel

    override val image: Bitmap
        get() = (imageView.drawable as BitmapDrawable).bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true

        if (arguments != null) {
            filmName = arguments!!.getString(FILM_NAME)!!
            omdbId = arguments!!.getString(OMDBID)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.details_fragment, container, false)

        viewModel = ViewModelProviders.of(
                this,
                DetailsViewModelFactory(activity!!.application, this))
                .get(DetailsViewModelImpl::class.java)

        imageView = rootView.findViewById(R.id.detail_poster)
        title = rootView.findViewById(R.id.film_name)
        details = rootView.findViewById(R.id.details)
        genre = rootView.findViewById(R.id.genre)
        plotSummary = rootView.findViewById(R.id.plot_summary)

        save = rootView.findViewById(R.id.save_image)
        save.setOnClickListener(this)

        sharedPreferences = (activity as MainActivity)
                .getSharedPreferences("offline", Context.MODE_PRIVATE)

        var checked = false

        if (sharedPreferences.contains(omdbId)) {
            checked = sharedPreferences.getBoolean(omdbId, false)
        }

        if (!checked)
            viewModel.initView(filmName)
        else
            viewModel.initViewById(omdbId)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        activity!!.title = "Details"
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (viewModel.onOptionItemSelected(item!!.itemId)) true else super.onOptionsItemSelected(item)

    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu!!.findItem(R.id.offline).isChecked = viewModel.isOffline
    }

    override fun onClick(v: View) {
        if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)

            }
        } else {
            viewModel.onSaveImageClicked(omdbId)
        }
    }

    override fun setImage(imageUri: Uri) {
        imageView.setImageURI(imageUri)
    }

    override fun initDetails(details: Details) {
        title.text = details.title
        this.details.text = details.details
        genre.text = details.genre
        plotSummary.text = details.plotSummary

        if (!EspressoIdlingResource.idlingResource.isIdleNow) {
            EspressoIdlingResource.decrement()
        }
    }

    fun saveImageToExStorage() {
        viewModel.onSaveImageClicked(omdbId)
    }

    override fun saveIsOffline() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(omdbId, true)
        editor.apply()
    }

    override fun backClicked() {
        HomeViewFragment.setIsSearchViewPopped(false)
        activity!!.onBackPressed()
    }

    override fun showWebView() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack("").replace(R.id.RelativeForFragments,
                WebViewFragment.newInstance(RetrofitModule.BASE_URL),
                WebViewFragment::class.java.simpleName)
        transaction.commit()
    }

    override fun showBrowser() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(RetrofitModule.BASE_URL))
        activity!!.startActivity(browserIntent)
    }

    override fun showAlertDialog() {
        val builder = AlertDialog.Builder(activity!!)

        builder.setTitle("Very dump message!")
                .setMessage("This...is..ALERT DIALOG!")
                .setCancelable(false)
                .setNegativeButton("ОК"
                ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()

        val handler = Handler()
        handler.postDelayed({ alert.show() }, 5000)
    }

    companion object {
        private val FILM_NAME = "FILM_NAME"
        private val OMDBID = "OMDBID"

        fun newInstance(filmName: String, omdbId: String): DetailsViewFragment {
            val fragment = DetailsViewFragment()
            val bundle = Bundle()
            bundle.putString(FILM_NAME, filmName)
            bundle.putString(OMDBID, omdbId.trim { it <= ' ' })
            fragment.arguments = bundle

            return fragment
        }
    }
}
