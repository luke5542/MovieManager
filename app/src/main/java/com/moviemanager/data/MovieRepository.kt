package com.moviemanager.data

import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDao: MovieDao
) {
    fun getMovieCount(): Int {
        return movieDao.count()
    }

    fun getAllMovies(): List<Movie> {
        return movieDao.getAllMovies().map {
            it.toUiMovie()
        }
    }

    fun upsertMovie(movie: Movie) {
        movieDao.upsertMovie(movie.toDbMovie())
    }

    fun removeMovie(movie: Movie) {
        movieDao.removeMovie(movie.toDbMovie())
    }

    private fun MovieEntity.toUiMovie(): Movie {
        return Movie(
            title = title,
            genre = genre,
            totalStock = totalStock,
            rentedStock = rentedStock
        )
    }

    private fun Movie.toDbMovie(): MovieEntity {
        return MovieEntity(
            title = title,
            genre = genre,
            totalStock = totalStock,
            rentedStock = rentedStock
        )
    }
}