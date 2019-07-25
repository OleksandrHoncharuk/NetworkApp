package com.example.networkaplication.home

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("app:customOnEditorActionListener")
fun setCustomOnEditorActionListener(view: TextView, listener: CustomOnEditorActionListener?) {
    if (listener == null) {
        view.setOnEditorActionListener(null)
    } else {
        view.setOnEditorActionListener { v, actionId, _ ->
            listener.onEditorAction(v, actionId)
            false
        }
    }
}


@BindingAdapter("app:customOnFocusChangeListener")
fun onFocusChange(view: View, listener: CustomOnFocusChangeListener?) {
    if (listener == null) {
        view.onFocusChangeListener = null
    } else {
        view.setOnFocusChangeListener { v, hasFocus ->

            listener.onFocusChanged(v, hasFocus)
        }
    }
}