package com.moviemanager.ui.movies

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.moviemanager.data.Movie
import com.moviemanager.data.MovieRepository
import com.moviemanager.data.SampleMovies
import com.moviemanager.ui.NavigationDestinations
import com.moviemanager.ui.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _state = MutableStateFlow<ViewState<MovieListState>>(ViewState.Loading())
    val state = _state.asStateFlow()

    init {
        addInitialMoviesIfNeeded()
    }

    fun onAddMovieClicked(navController: NavController) {
        navController.navigate(NavigationDestinations.AddMovie.key)
    }

    private fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = repository.getAllMovies()
            launch(Dispatchers.Main) {
                _state.update {
                    ViewState.Success(MovieListState(movies))
                }
            }
        }
    }

    private fun addInitialMoviesIfNeeded() {
        viewModelScope.launch(Dispatchers.IO) {
            val movieCount = repository.getMovieCount()
            if (movieCount == 0) {
                SampleMovies.sampleMovies.forEach {
                    repository.upsertMovie(it)
                }
            }
            loadMovies()
        }
    }
}

data class MovieListState(
    val movies: List<Movie>
)


