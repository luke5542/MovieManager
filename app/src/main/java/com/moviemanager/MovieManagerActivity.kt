package com.moviemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviemanager.ui.NavigationDestinations
import com.moviemanager.ui.movies.AddMovie
import com.moviemanager.ui.movies.AddMovieViewModel
import com.moviemanager.ui.movies.MovieList
import com.moviemanager.ui.movies.MovieListViewModel
import com.moviemanager.ui.theme.MovieManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestinations.MovieList.key
                    ) {
                        composable(NavigationDestinations.MovieList.key) {
                            val viewModel = hiltViewModel<MovieListViewModel>()
                            MovieList(navController, viewModel)
                        }
                        composable(NavigationDestinations.AddMovie.key) {
                            val viewModel = hiltViewModel<AddMovieViewModel>()
                            AddMovie(navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}