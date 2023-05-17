package com.moviemanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Query("SELECT * FROM movieentity")
    fun getAllMovies(): List<MovieEntity>

    @Upsert
    fun upsertMovie(movie: MovieEntity)

    @Delete
    fun removeMovie(movie: MovieEntity)

    @Query("SELECT COUNT(title) FROM movieentity")
    fun count(): Int
}

@Entity
data class MovieEntity(
    @PrimaryKey val title: String,
    val genre: String,
    val totalStock: Int,
    val rentedStock: Int
)