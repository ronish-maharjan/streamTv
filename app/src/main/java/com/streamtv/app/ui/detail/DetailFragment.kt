package com.streamtv.app.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import coil.imageLoader
import coil.request.ImageRequest
import com.streamtv.app.data.prefs.AppPrefs
import com.streamtv.app.ui.player.PlayerActivity

class DetailFragment : DetailsSupportFragment() {

    private lateinit var bgController: DetailsSupportFragmentBackgroundController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bgController = DetailsSupportFragmentBackgroundController(this)

        val prefs = AppPrefs(requireContext())

        val title = arguments?.getString(DetailActivity.EXTRA_TITLE) ?: "Movie"
        val description = arguments?.getString(DetailActivity.EXTRA_DESCRIPTION) ?: ""
        val movieId = arguments?.getString(DetailActivity.EXTRA_MOVIE_ID) ?: ""
        val thumbnail = arguments?.getString(DetailActivity.EXTRA_THUMBNAIL)

        // ✅ Build stream URL from saved IP — never trust server-returned URL
        val streamUrl = "http://${prefs.serverIp}/stream/$movieId"

        val row = DetailsOverviewRow(title)
        row.actionsAdapter = SparseArrayObjectAdapter().apply {
            set(0, Action(0, "▶  Play Now"))
        }

        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(
            object : AbstractDetailsDescriptionPresenter() {
                override fun onBindDescription(vh: ViewHolder, item: Any) {
                    vh.title.text = item as String
                    vh.subtitle.text = description
                }
            }
        )

        detailsPresenter.backgroundColor = 0xFF1a1a2e.toInt()

        detailsPresenter.setOnActionClickedListener { action ->
            if (action.id == 0L) {
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_TITLE, title)
                }
                startActivity(intent)
            }
        }

        val presenterSelector = ClassPresenterSelector().apply {
            addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
        }

        val adapter = ArrayObjectAdapter(presenterSelector)
        adapter.add(row)
        this.adapter = adapter

        if (thumbnail != null) {
            val request = ImageRequest.Builder(requireContext())
                .data(thumbnail)
                .target { drawable ->
                    bgController.enableParallax()
                    row.imageDrawable = drawable
                }
                .build()
            requireContext().imageLoader.enqueue(request)
        }
    }
}
