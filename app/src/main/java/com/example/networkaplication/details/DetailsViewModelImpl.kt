package com.example.networkaplication.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.networkaplication.R
import com.example.networkaplication.models.select.MovieDetail
import com.example.networkaplication.persistance.model.Details

class DetailsViewModelImpl internal constructor(app: Application, private val view: DetailsContract.DetailsView) : AndroidViewModel(app), DetailsContract.DetailsViewModel, DetailsCallback {

    private val repository: DetailsContract.DetailsRepository
    private var details: Details? = null
    override var isOffline = false

    init {
        repository = DetailsRepositoryImpl(app, this)
    }

    override fun onOptionItemSelected(itemId: Int): Boolean {
        when (itemId) {
            android.R.id.home -> {
                backClicked()
                return true
            }

            R.id.web_view_item -> {
                showWebView()
                return true
            }

            R.id.browser -> {
                showBrowser()
                return true
            }

            R.id.alert_dialog -> {
                showAlertDialog()
                return true
            }

            R.id.offline -> {
                saveIsOffline()
                return true
            }

            else -> return false
        }
    }

    override fun initView(filmName: String) {
        repository.loadDetails(filmName)
    }

    override fun initViewById(omdbId: String) {
        view.setImage(repository.loadImage(omdbId)!!)
        view.initDetails(repository.getDetailFromId(omdbId))
    }

    override fun onSaveImageClicked(omdbId: String) {
        repository.saveImage(view.image, omdbId)
    }

    override fun onMoviesReceived(movie: MovieDetail) {
        setImage(movie.poster)

        details = Details(movie.imdbID!!)
        details!!.title = movie.title
        details!!.genre = movie.genre

        val detailsLine = String
                .format("%s %s %s %s/10 ", movie.year, movie.runtime,
                        movie.country, movie.imdbRating)
        details!!.details = detailsLine
        details!!.plotSummary = movie.plot

        view.initDetails(details!!)
    }

    private fun setImage(poster: String?) {
        Glide
                .with(view.imageView.context)
                .load(poster)
                .apply(RequestOptions().optionalTransform(CropSquareTransformation()))
                .into(view.imageView)
    }

    override fun onDetailsReceived(details: Details) {
        this.details = details
        view.initDetails(details)
    }

    private fun backClicked() {
        view.backClicked()
    }

    private fun showWebView() {
        view.showWebView()
    }

    private fun showBrowser() {
        view.showBrowser()
    }

    private fun showAlertDialog() {
        view.showAlertDialog()
    }

    private fun saveIsOffline() {
        isOffline = !isOffline
        view.saveIsOffline()
    }
}
