package com.streamtv.app.ui.player

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.DefaultLoadControl
import com.streamtv.app.databinding.ActivityPlayerBinding

class PlayerActivity : FragmentActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val streamUrl = intent.getStringExtra(EXTRA_STREAM_URL) ?: return
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""

        binding.playerTitle.text = title

        initPlayer(streamUrl)
    }

    private fun initPlayer(streamUrl: String) {
        // Tune buffer for low-end device + local network
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                5_000,   // min buffer
                20_000,  // max buffer
                1_500,   // buffer before playback start
                3_000    // buffer before playback resume after rebuffer
            )
            .build()

        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .build()
            .also { exo ->
                binding.playerView.player = exo
                val mediaItem = MediaItem.fromUri(Uri.parse(streamUrl))
                exo.setMediaItem(mediaItem)
                exo.prepare()
                exo.playWhenReady = true

                exo.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        binding.playerView.keepScreenOn = isPlaying
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
