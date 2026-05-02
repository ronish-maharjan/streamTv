package com.streamtv.app.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import coil.imageLoader
import coil.request.ImageRequest
import com.streamtv.app.ui.player.PlayerActivity

class DetailFragment : DetailsSupportFragment() {

    private lateinit var bgController: DetailsSupportFragmentBackgroundController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bgController = DetailsSupportFragmentBackgroundController(this)

        val title = arguments?.getString(DetailActivity.EXTRA_TITLE) ?: "Movie"
        val description = arguments?.getString(DetailActivity.EXTRA_DESCRIPTION) ?: ""
        val streamUrl = arguments?.getString(DetailActivity.EXTRA_STREAM_URL) ?: ""
        val thumbnail = arguments?.getString(DetailActivity.EXTRA_THUMBNAIL)

        // Build detail row
        val row = DetailsOverviewRow(title)
        row.actionsAdapter = SparseArrayObjectAdapter().apply {
            set(0, Action(0, "▶  Play Now"))
        }

        val presenterSelector = ClassPresenterSelector().apply {
            val detailsPresenter = FullWidthDetailsOverviewRowPresenter(
                object : AbstractDetailsDescriptionPresenter() {
                    override fun onBindDescription(vh: ViewHolder, item: Any) {
                        vh.title.text = item as String
                        vh.subtitle.text = description
                    }
                }
            )
            detailsPresenter.backgroundColor = 0xFF1a1a2e.toInt()
            addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
        }

        val adapter = ArrayObjectAdapter(presenterSelector)
        adapter.add(row)
        this.adapter = adapter

        // Load background
        if (thumbnail != null) {
            val request = ImageRequest.Builder(requireContext())
                .data(thumbnail)
                .target { drawable ->
                    bgController.enableParallax()
                    bgController.coverBitmap = null
                    row.imageDrawable = drawable
                }
                .build()
            requireContext().imageLoader.enqueue(request)
        }

        onActionClickedListener = OnActionClickedListener { action ->
            if (action.id == 0L) {
                val intent = Intent(requireContext(), PlayerActivity::class.java)
                intent.putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                intent.putExtra(PlayerActivity.EXTRA_TITLE, title)
                startActivity(intent)
            }
        }
    }
}
