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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BrowseSupportFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var prefs: AppPrefs
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        headersState = HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = false
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

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            if (item is Movie) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, item.id)
                intent.putExtra(DetailActivity.EXTRA_STREAM_URL, item.streamUrl)
                intent.putExtra(DetailActivity.EXTRA_TITLE, item.title)
                intent.putExtra(DetailActivity.EXTRA_THUMBNAIL, item.thumbnail)
                intent.putExtra(DetailActivity.EXTRA_DESCRIPTION, item.description)
                startActivity(intent)
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
    }

    private fun showMovies(movies: List<Movie>) {
        rowsAdapter.clear()

        val cardPresenter = MovieCardPresenter()
        val movieAdapter = ArrayObjectAdapter(cardPresenter)
        movies.forEach { movieAdapter.add(it) }

        val header = HeaderItem(0, "All Movies  (${movies.size})")
        rowsAdapter.add(ListRow(header, movieAdapter))
    }

    private fun showError(message: String) {
        rowsAdapter.clear()
        // Show error row
        val cardPresenter = MovieCardPresenter()
        val emptyAdapter = ArrayObjectAdapter(cardPresenter)
        val header = HeaderItem(0, "⚠️ $message")
        rowsAdapter.add(ListRow(header, emptyAdapter))
    }
}
