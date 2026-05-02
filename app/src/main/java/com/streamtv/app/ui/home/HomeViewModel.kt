package com.streamtv.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamtv.app.data.api.ApiClient
import com.streamtv.app.data.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MoviesState {
    object Loading : MoviesState()
    data class Success(val movies: List<Movie>) : MoviesState()
    data class Error(val message: String) : MoviesState()
}

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow<MoviesState>(MoviesState.Loading)
    val state: StateFlow<MoviesState> = _state

    fun loadMovies(baseUrl: String, apiKey: String) {
        viewModelScope.launch {
            _state.value = MoviesState.Loading
            try {
                val response = ApiClient.getService(baseUrl).getMovies(apiKey)
                if (response.success && response.data != null) {
                    _state.value = MoviesState.Success(response.data)
                } else {
                    _state.value = MoviesState.Error(response.error ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = MoviesState.Error("Cannot connect to server: ${e.message}")
            }
        }
    }
}
