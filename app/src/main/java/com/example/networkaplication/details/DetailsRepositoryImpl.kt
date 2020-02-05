package com.example.networkaplication.details

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import com.example.networkaplication.App
import com.example.networkaplication.idling.EspressoIdlingResource
import com.example.networkaplication.models.select.MovieDetail
import com.example.networkaplication.persistance.model.Details
import com.example.networkaplication.persistance.repository.Repositories
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl
import com.example.networkaplication.persistance.repository.database.dao.DetailsDao
import com.example.networkaplication.retrofit.OMDBApiInterface
import com.example.networkaplication.retrofit.component.DaggerDetailsComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class DetailsRepositoryImpl internal constructor(app: Application, private val callback: DetailsCallback) : DetailsContract.DetailsRepository, Callback<MovieDetail> {

    private val detailsDao: DetailsDao

    @Inject
    lateinit var omdbApiInterface: OMDBApiInterface


    init {
        val repository = Repositories.getDatabase(app.applicationContext) as DatabaseRepositoryImpl

        val component = DaggerDetailsComponent.builder()
                .appComponent((app as App).component)
                .build()
        component.inject(this)

        detailsDao = repository.detailsDao()
    }

    override fun saveImage(imageToSave: Bitmap, omdbId: String) {
        val file = File(root, omdbId.trim { it <= ' ' } + ".PNG")

        try {
            val out = FileOutputStream(file)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            out.write(byteArrayOutputStream.toByteArray())

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun loadImage(omdbId: String): Uri? {
        val file = File(root, omdbId.trim { it <= ' ' } + ".PNG")

        return if (file.exists()) Uri.fromFile(file) else null

    }

    override fun loadDetails(filmName: String) {
        EspressoIdlingResource.increment()

        omdbApiInterface.getMovieDetails(filmName).enqueue(this)
    }

    override fun getDetailFromId(omdbId: String): Details {
        return detailsDao.findById(omdbId)
    }

    override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
        val movie = response.body()

        if (movie != null) {

            callback.onMoviesReceived(movie)
            val detailsLine = String
                    .format("%s %s %s %s/10 ", movie.year, movie.runtime,
                            movie.country, movie.imdbRating)

            val details = detailsDao.findByTitle(movie.title!!)

            if (details == null) {
                val details1 = Details(movie.imdbID!!)
                details1.title = movie.title
                details1.genre = movie.genre
                details1.details = detailsLine
                details1.plotSummary = movie.plot
//                callback.onDetailsReceived(details1)
                detailsDao.insert(details1)
            } else {
                details.title = movie.title
                details.genre = movie.genre
                details.details = detailsLine
                details.plotSummary = movie.plot
//                callback.onDetailsReceived(details)
                detailsDao.update(details)
            }

        }
    }

    override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
        call.cancel()
    }

    companion object {

        private val root = Environment.getExternalStorageDirectory()
    }
}
