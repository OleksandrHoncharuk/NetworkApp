package com.example.networkaplication.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.networkaplication.R

class HomeAdapter(private val presenter: ListPresenter) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var onFilmClickedListener: OnFilmClickedListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_layout, null))
    }

    fun setOnFilmClickedListener(onFilmClickedListener: OnFilmClickedListener) {
        this.onFilmClickedListener = onFilmClickedListener
    }

    override fun onBindViewHolder(viewHolder: HomeViewHolder, i: Int) {
        presenter.onBindRowViewAtPosition(viewHolder, i)
    }

    override fun getItemCount(): Int {
        return presenter.rowsCount
    }

    interface OnFilmClickedListener {
        fun onClicked(itemData: ItemData)
    }

    inner class HomeViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), RowView, View.OnClickListener {
        private val image: ImageView
        internal val title: TextView

        init {

            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.film_poster)
            title = itemView.findViewById(R.id.film_title)
        }

        override fun setImage(imageUrl: String) {
            Glide.with(itemView)
                    .load(imageUrl)
                    .into(image)
        }

        override fun setTitle(title: String) {
            this.title.text = title
        }

        override fun onClick(v: View) {
            onFilmClickedListener!!.onClicked(presenter[adapterPosition])
        }
    }

}
