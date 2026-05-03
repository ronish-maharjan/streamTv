package com.streamtv.app.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import coil.load
import coil.transform.RoundedCornersTransformation
import com.streamtv.app.R
import com.streamtv.app.data.prefs.AppPrefs
import com.streamtv.app.ui.player.PlayerActivity

class DetailActivity : FragmentActivity() {

    private lateinit var prefs: AppPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        prefs = AppPrefs(this)

        val movieId    = intent.getStringExtra(EXTRA_MOVIE_ID) ?: ""
        val title      = intent.getStringExtra(EXTRA_TITLE) ?: "Unknown"
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: "No description available."
        val thumbnail  = intent.getStringExtra(EXTRA_THUMBNAIL)
        val sizeBytes  = intent.getLongExtra(EXTRA_SIZE, 0L)

        // Stream URL built from saved IP
        val streamUrl = "http://${prefs.serverIp}/stream/$movieId"

        // Bind views
        val titleView       = findViewById<TextView>(R.id.detail_title)
        val descView        = findViewById<TextView>(R.id.detail_description)
        val sizeView        = findViewById<TextView>(R.id.detail_size)
        val poster          = findViewById<ImageView>(R.id.detail_poster)
        val bgImage         = findViewById<ImageView>(R.id.detail_bg)
        val btnPlay         = findViewById<View>(R.id.btn_play)
        val btnBack         = findViewById<View>(R.id.btn_back_detail)

        titleView.text = title
        descView.text = description
        sizeView.text = formatSize(sizeBytes)

        // Load poster
        if (!thumbnail.isNullOrEmpty()) {
            poster.load(thumbnail) {
                crossfade(300)
                transformations(RoundedCornersTransformation(4f))
            }
            // Blurred background
            bgImage.load(thumbnail) {
                crossfade(500)
            }
        }

        // Play button
        btnPlay.setOnClickListener {
            startActivity(
                Intent(this, PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_TITLE, title)
                }
            )
        }

        // Back button
        btnBack.setOnClickListener { finish() }

        // ✅ Focus on play button by default
        btnPlay.requestFocus()
    }

    // ✅ Handle D-pad back button
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK ||
            keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun formatSize(bytes: Long): String {
        if (bytes <= 0) return "Unknown size"
        val gb = bytes / (1024.0 * 1024.0 * 1024.0)
        val mb = bytes / (1024.0 * 1024.0)
        return if (gb >= 1) "%.1f GB".format(gb) else "%.0f MB".format(mb)
    }

    companion object {
        const val EXTRA_MOVIE_ID    = "movie_id"
        const val EXTRA_STREAM_URL  = "stream_url"
        const val EXTRA_TITLE       = "title"
        const val EXTRA_THUMBNAIL   = "thumbnail"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_SIZE        = "size"
    }
}
