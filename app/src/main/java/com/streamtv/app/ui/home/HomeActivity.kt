package com.streamtv.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.streamtv.app.data.prefs.AppPrefs
import com.streamtv.app.databinding.ActivityHomeBinding
import com.streamtv.app.ui.setup.SetupActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : FragmentActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var prefs: AppPrefs
    private var fragmentAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = AppPrefs(this)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.btnRetry.setOnClickListener {
            viewModel.loadMovies(prefs.getBaseUrl(), prefs.apiKey)
        }

        binding.btnGoSettings.setOnClickListener {
            startActivity(Intent(this, SetupActivity::class.java))
        }

        observeState()
        viewModel.loadMovies(prefs.getBaseUrl(), prefs.apiKey)
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is MoviesState.Loading -> {
                        binding.loadingView.visibility = View.VISIBLE
                        binding.errorView.visibility = View.GONE
                    }
                    is MoviesState.Success -> {
                        binding.loadingView.visibility = View.GONE
                        binding.errorView.visibility = View.GONE
                        if (!fragmentAdded) {
                            fragmentAdded = true
                            supportFragmentManager.beginTransaction()
                                .replace(
                                    com.streamtv.app.R.id.home_fragment_container,
                                    HomeFragment()
                                )
                                .commit()
                        }
                    }
                    is MoviesState.Error -> {
                        binding.loadingView.visibility = View.GONE
                        binding.errorView.visibility = View.VISIBLE
                        binding.errorMessage.text = state.message
                    }
                }
            }
        }
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            startActivity(Intent(this, SetupActivity::class.java))
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }
}
