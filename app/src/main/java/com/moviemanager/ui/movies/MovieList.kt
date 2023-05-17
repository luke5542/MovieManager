@file:OptIn(
    ExperimentalMaterial3Api::class
)

package com.moviemanager.ui.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.moviemanager.R
import com.moviemanager.data.Movie
import com.moviemanager.data.SampleMovies
import com.moviemanager.ui.ViewState
import com.moviemanager.ui.theme.MovieManagerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MovieList(
    navController: NavController,
    viewModel: MovieListViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val modelState by viewModel.state.collectAsState()
    val state = modelState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    DisposableEffect(key1 = lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        onDispose { lifecycleOwner.lifecycle.removeObserver(viewModel) }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { MoviesTopBar() },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.onAddMovieClicked(navController)
                }
            ) { Text(stringResource(R.string.fab_add_movie)) }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when (state) {
                    is ViewState.Error -> LoadingError(coroutineScope, snackbarHostState, state.message)
                    is ViewState.Loading -> Loading()
                    is ViewState.Success -> LoadedMovieList(state.data)
                }
            }
        }
    )
}

@Composable
private fun MoviesTopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.movies_title)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun Loading() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun LoadingError(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: String
) {
    LaunchedEffect(key1 = message) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message
            )
        }
    }
}

@Composable
private fun LoadedMovieList(state: MovieListState) {
    LazyColumn {
        itemsIndexed(state.movies) {index, item ->
            MovieItem(item)
            if (index < state.movies.lastIndex)
                Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
private fun MovieItem(movie: Movie) {
    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val (
            titleText, genreText, stockText, rentedText, divider
        ) = createRefs()
        val barrier = createStartBarrier(stockText, rentedText)
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 3,
            modifier = Modifier.constrainAs(titleText) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 16.dp)
                top.linkTo(parent.top, 16.dp)
                end.linkTo(stockText.start)
            }
        )
        Text(
            text = movie.genre,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.constrainAs(genreText) {
                start.linkTo(parent.start, 16.dp)
                top.linkTo(titleText.bottom)
            }
        )
        Text(
            text = stringResource(R.string.rented_quanitity_label, movie.rentedStock),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.constrainAs(rentedText) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(parent.top, 16.dp)
                bottom.linkTo(divider.top, 8.dp)
            }
        )
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .constrainAs(divider) {
                    width = Dimension.fillToConstraints
                    start.linkTo(barrier)
                    end.linkTo(rentedText.end)
                    top.linkTo(rentedText.bottom)
                    bottom.linkTo(stockText.top)
                }
        )
        Text(
            text = stringResource(R.string.stock_quanitity_label, movie.totalStock),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.constrainAs(stockText) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(divider.bottom, 8.dp)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MovieManagerTheme {
        MovieItem(Movie("Interstellar", "SciFi"))
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    MovieManagerTheme {
        LoadedMovieList(MovieListState(SampleMovies.sampleMovies))
    }
}