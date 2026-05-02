package com.streamtv.app.ui.home

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.streamtv.app.R
import com.streamtv.app.data.model.Movie
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

class MovieCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_card, parent, false)
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        val view = viewHolder.view

        val thumbnail = view.findViewById<ImageView>(R.id.movie_thumbnail)
        val title = view.findViewById<TextView>(R.id.movie_title)

        title.text = movie.title

        if (movie.thumbnail != null) {
            thumbnail.load(movie.thumbnail) {
                crossfade(true)
                transformations(RoundedCornersTransformation(8f))
                error(android.R.drawable.ic_menu_gallery)
            }
        } else {
            thumbnail.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val thumbnail = viewHolder.view.findViewById<ImageView>(R.id.movie_thumbnail)
        thumbnail.setImageDrawable(null)
    }
}
