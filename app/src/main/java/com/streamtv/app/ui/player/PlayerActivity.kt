package com.streamtv.app.ui.player

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.streamtv.app.data.prefs.AppPrefs
import com.streamtv.app.databinding.ActivityPlayerBinding

class PlayerActivity : FragmentActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var prefs: AppPrefs
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = AppPrefs(this)

        val streamUrl = intent.getStringExtra(EXTRA_STREAM_URL)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""

        if (streamUrl.isNullOrEmpty()) {
            Toast.makeText(this, "No stream URL provided", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        //binding.playerTitle.text = title
        initPlayer(streamUrl)
    }

    private fun initPlayer(streamUrl: String) {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                5_000,
                20_000,
                1_500,
                3_000
            )
            .build()

        // ✅ Add API key header to every request ExoPlayer makes
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(
                mapOf("x-api-key" to prefs.apiKey)
            )
            .setConnectTimeoutMs(10_000)
            .setReadTimeoutMs(20_000)

        val mediaSource = ProgressiveMediaSource.Factory(httpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(streamUrl)))

        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .build()
            .also { exo ->
                binding.playerView.player = exo
                exo.setMediaSource(mediaSource)
                exo.prepare()
                exo.playWhenReady = true

                exo.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        binding.playerView.keepScreenOn = isPlaying
                    }

                    // ✅ Show error if something goes wrong
                    override fun onPlayerError(error: PlaybackException) {
                        Toast.makeText(
                            this@PlayerActivity,
                            "Playback error: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.playerView.dispatchKeyEvent(
            KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
        ) || super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }

    companion object {
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_TITLE = "title"
    }
}
