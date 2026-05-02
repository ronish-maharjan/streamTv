package com.streamtv.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.streamtv.app.data.model.Movie
import com.streamtv.app.data.prefs.AppPrefs
import com.streamtv.app.ui.detail.DetailActivity
import com.streamtv.app.ui.setup.SetupActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BrowseSupportFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var prefs: AppPrefs
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        headersState = HEADERS_ENABLED   // ✅ show left sidebar
        isHeadersTransitionOnBackEnabled = true
        title = "🎬 StreamTV"
        brandColor = 0xFF1a1a2e.toInt()
        searchAffordanceColor = 0xFFe50914.toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = AppPrefs(requireContext())
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        setupAdapter()
        observeState()

        viewModel.loadMovies(prefs.getBaseUrl(), prefs.apiKey)

        // ✅ Handle clicks on both movies and settings row
        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, row ->
            when (item) {
                is Movie -> {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, item.id)
                    intent.putExtra(DetailActivity.EXTRA_STREAM_URL, item.streamUrl)
                    intent.putExtra(DetailActivity.EXTRA_TITLE, item.title)
                    intent.putExtra(DetailActivity.EXTRA_THUMBNAIL, item.thumbnail)
                    intent.putExtra(DetailActivity.EXTRA_DESCRIPTION, item.description)
                    startActivity(intent)
                }
                is String -> {
                    // Settings item clicked
                    if (item == "⚙️ Change Server Settings") {
                        startActivity(Intent(requireContext(), SetupActivity::class.java))
                    } else if (item == "🔄 Refresh") {
                        viewModel.loadMovies(prefs.getBaseUrl(), prefs.apiKey)
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is MoviesState.Loading -> showLoading()
                    is MoviesState.Success -> showMovies(state.movies)
                    is MoviesState.Error -> showError(state.message)
                }
            }
        }
    }

    private fun showLoading() {
        rowsAdapter.clear()
        addSettingsRow() // ✅ always show settings even while loading
    }

    private fun showMovies(movies: List<Movie>) {
        rowsAdapter.clear()

        // ✅ Movies row
        val cardPresenter = MovieCardPresenter()
        val movieAdapter = ArrayObjectAdapter(cardPresenter)
        movies.forEach { movieAdapter.add(it) }
        rowsAdapter.add(ListRow(HeaderItem(0, "🎬 All Movies (${movies.size})"), movieAdapter))

        addSettingsRow()
    }

    private fun showError(message: String) {
        rowsAdapter.clear()
        val cardPresenter = MovieCardPresenter()
        val emptyAdapter = ArrayObjectAdapter(cardPresenter)
        rowsAdapter.add(ListRow(HeaderItem(0, "⚠️ $message"), emptyAdapter))
        addSettingsRow()
    }

    // ✅ Settings row always visible at the bottom
    private fun addSettingsRow() {
        val settingsPresenter = object : Presenter() {
            override fun onCreateViewHolder(parent: android.view.ViewGroup): ViewHolder {
                val tv = android.widget.TextView(parent.context).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams(320, 120)
                    textSize = 16f
                    setTextColor(0xFFFFFFFF.toInt())
                    gravity = android.view.Gravity.CENTER
                    setPadding(24, 0, 24, 0)
                    isFocusable = true
                    isFocusableInTouchMode = true
                    background = android.graphics.drawable.StateListDrawable().apply {
                        // focused state
                        val focused = android.graphics.drawable.GradientDrawable().apply {
                            setColor(0xFFe50914.toInt())
                            cornerRadius = 12f
                        }
                        // normal state
                        val normal = android.graphics.drawable.GradientDrawable().apply {
                            setColor(0xFF333344.toInt())
                            cornerRadius = 12f
                        }
                        addState(intArrayOf(android.R.attr.state_focused), focused)
                        addState(intArrayOf(), normal)
                    }
                }
                return ViewHolder(tv)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
                (viewHolder.view as android.widget.TextView).text = item as String
            }

            override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
        }

        val settingsAdapter = ArrayObjectAdapter(settingsPresenter)
        settingsAdapter.add("⚙️ Change Server Settings")
        settingsAdapter.add("🔄 Refresh")

        rowsAdapter.add(
            ListRow(HeaderItem(1, "Settings"), settingsAdapter)
        )
    }
}
