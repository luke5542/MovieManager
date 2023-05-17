package com.moviemanager.data

data class Movie(
    val title: String,
    val genre: String,
    val totalStock: Int = 1,
    val rentedStock: Int = 0
)