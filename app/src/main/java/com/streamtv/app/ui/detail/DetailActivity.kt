package com.streamtv.app.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.streamtv.app.R
import com.streamtv.app.ui.player.PlayerActivity

class DetailActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            val fragment = DetailFragment().apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, fragment)
                .commit()
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_TITLE = "title"
        const val EXTRA_THUMBNAIL = "thumbnail"
        const val EXTRA_DESCRIPTION = "description"
    }
}
