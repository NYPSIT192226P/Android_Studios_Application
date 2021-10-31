package com.nyp.sit.movieviewer_intermediate_starter_project

import androidx.room.*
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.Movie
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("Select * from movielist_table")
    fun retrieveAllMovies() : Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newMovie: Movie)

    @Delete
    fun delete(delMovie: Movie)

}