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
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        title = "StreamTV"
        brandColor = 0xFF0A0A0F.toInt()
        searchAffordanceColor = 0xFFE50914.toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = AppPrefs(requireContext())
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter

        observeState()

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is Movie -> {
                    val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_MOVIE_ID, item.id)
                        putExtra(DetailActivity.EXTRA_TITLE, item.title)
                        putExtra(DetailActivity.EXTRA_THUMBNAIL, item.thumbnail)
                        putExtra(DetailActivity.EXTRA_DESCRIPTION, item.description)
                        putExtra(DetailActivity.EXTRA_SIZE, item.size)      // ← add this
                    }
                    startActivity(intent)
                }
            }
            is SettingsItem -> {
                when (item.id) {
                    SettingsItem.ID_SETTINGS ->
                        startActivity(Intent(requireContext(), SetupActivity::class.java))
                        SettingsItem.ID_REFRESH ->
                            viewModel.loadMovies(prefs.getBaseUrl(), prefs.apiKey)
                        }
                    }
                }
            }
        }

        private fun observeState() {
            lifecycleScope.launch {
                viewModel.state.collectLatest { state ->
                    if (state is MoviesState.Success) {
                        buildRows(state.movies)
                    }
                }
            }
        }

        private fun buildRows(movies: List<Movie>) {
            rowsAdapter.clear()

            // Movies row
            val cardPresenter = MovieCardPresenter()
            val movieAdapter = ArrayObjectAdapter(cardPresenter)
            movies.forEach { movieAdapter.add(it) }
            rowsAdapter.add(
                ListRow(HeaderItem(0, "All Movies  •  ${movies.size} titles"), movieAdapter)
            )

            // Settings row — clean minimal buttons
            val settingsPresenter = SettingsItemPresenter()
            val settingsAdapter = ArrayObjectAdapter(settingsPresenter)
            settingsAdapter.add(SettingsItem(SettingsItem.ID_REFRESH, "⟳  Refresh"))
            settingsAdapter.add(SettingsItem(SettingsItem.ID_SETTINGS, "⚙  Settings"))
            rowsAdapter.add(ListRow(HeaderItem(1, ""), settingsAdapter))
        }
    }

    // Clean data class for settings items
    data class SettingsItem(val id: Int, val label: String) {
        companion object {
            const val ID_REFRESH = 1
            const val ID_SETTINGS = 2
        }
    }
