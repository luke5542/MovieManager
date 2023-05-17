@file:OptIn(ExperimentalMaterial3Api::class)

package com.moviemanager.ui.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moviemanager.R
import com.moviemanager.ui.theme.MovieManagerTheme

@Composable
fun AddMovie(
    navController: NavController,
    addMovieViewModel: AddMovieViewModel
) {
    Scaffold(
        topBar = { AddMovieTopBar() },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                AddMovie(
                    onTitleUpdate = {
                        addMovieViewModel.onTitleTextEntered(it)
                    },
                    onGenreUpdate = {
                        addMovieViewModel.onGenreTextEntered(it)
                    },
                    onStockUpdate = {
                        addMovieViewModel.onStockUpdated(it)
                    },
                    onAddMovieClick = {
                        addMovieViewModel.onAddButtonClick(navController)
                    }
                )
            }
        }
    )
}

@Composable
private fun AddMovieTopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.add_movie_title)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun AddMovie(
    onTitleUpdate: (String) -> Unit,
    onGenreUpdate: (String) -> Unit,
    onStockUpdate: (Int) -> Unit,
    onAddMovieClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var titleText by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = titleText,
            singleLine = true,
            label = { Text("Title") },
            onValueChange = {
                titleText = it
                onTitleUpdate.invoke(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 8.dp
                )
        )
        var genreText by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = genreText,
            singleLine = true,
            label = { Text("Genre") },
            onValueChange = {
                genreText = it
                onGenreUpdate.invoke(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp
                )
        )
        var stockText by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = stockText,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Total Stock") },
            onValueChange = {
                stockText = it
                onStockUpdate.invoke(it.toIntOrNull() ?: 0)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp
                )
        )
        Button(
            onClick = onAddMovieClick,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp
            )
        ) {
            Text("Add Movie")
        }
    }
}

@Preview
@Composable
private fun AddMoviePreview() {
    MovieManagerTheme {
        AddMovie({}, {}, {}, {})
    }
}
