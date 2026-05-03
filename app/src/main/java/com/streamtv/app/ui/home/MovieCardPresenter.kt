package com.streamtv.app.ui.home

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.streamtv.app.R
import com.streamtv.app.data.model.Movie

class MovieCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_card, parent, false)
        view.isFocusable = true
        view.isFocusableInTouchMode = true

        // ✅ Scale up smoothly on focus, back on blur
        view.setOnFocusChangeListener { v, hasFocus ->
            val scale = if (hasFocus) 1.08f else 1.0f
            v.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(150)
                .start()

            // Bring focused card to front so border isn't clipped
            if (hasFocus) v.bringToFront()
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        val view = viewHolder.view

        val thumbnail = view.findViewById<ImageView>(R.id.movie_thumbnail)
        val title = view.findViewById<TextView>(R.id.movie_title)
        val placeholder = view.findViewById<View>(R.id.placeholder)
        val placeholderTitle = view.findViewById<TextView>(R.id.placeholder_title)

        title.text = movie.title
        placeholderTitle.text = movie.title

        if (!movie.thumbnail.isNullOrEmpty()) {
            placeholder.visibility = View.GONE
            thumbnail.visibility = View.VISIBLE
            thumbnail.load(movie.thumbnail) {
                crossfade(250)
                transformations(RoundedCornersTransformation(10f))
                listener(
                    onError = { _, _ ->
                        thumbnail.visibility = View.GONE
                        placeholder.visibility = View.VISIBLE
                    }
                )
            }
        } else {
            thumbnail.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val thumbnail = viewHolder.view.findViewById<ImageView>(R.id.movie_thumbnail)
        thumbnail.setImageDrawable(null)
        // Reset scale when recycled
        viewHolder.view.scaleX = 1.0f
        viewHolder.view.scaleY = 1.0f
    }
}
