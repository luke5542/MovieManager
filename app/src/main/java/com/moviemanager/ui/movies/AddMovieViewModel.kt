package com.moviemanager.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.moviemanager.data.Movie
import com.moviemanager.data.MovieRepository
import com.moviemanager.ui.NavigationDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    var title: String = ""
    var genre: String = ""
    var stock: Int = 0

    fun onTitleTextEntered(text: String) {
        title = text
    }

    fun onGenreTextEntered(text: String) {
        genre = text
    }

    fun onStockUpdated(stock: Int) {
        this.stock = stock
    }

    fun onAddButtonClick(navController: NavController) {
        val movie = Movie(
            title = title,
            genre = genre,
            totalStock = stock,
            rentedStock = 0
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsertMovie(movie)
            launch(Dispatchers.Main) {
                navController.navigate(NavigationDestinations.MovieList.key)
            }
        }
    }
}