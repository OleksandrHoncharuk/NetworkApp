package com.example.networkaplication.details

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView

import com.example.networkaplication.persistance.model.Details

interface DetailsContract {

    interface DetailsView {

        val image: Bitmap

        val imageView: ImageView

        fun setImage(imageUri: Uri)

        fun initDetails(details: Details)

        fun saveIsOffline()

        fun backClicked()

        fun showWebView()

        fun showBrowser()

        fun showAlertDialog()

        fun getPermission()
    }

    interface DetailsViewModel {

        var isOffline: Boolean

        fun onOptionItemSelected(itemId: Int): Boolean

        fun initView(filmName: String)

        fun initViewById(omdbId: String)

        fun onSaveImageClicked(omdbId: String)
    }


    interface DetailsRepository {

        fun saveImage(imageToSave: Bitmap, omdbId: String)

        fun loadImage(omdbId: String): Uri?

        fun loadDetails(filmName: String)

        fun getDetailFromId(omdbId: String): Details
    }
}
