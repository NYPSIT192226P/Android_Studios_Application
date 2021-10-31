package com.nyp.sit.movieviewer_intermediate_starter_project

import android.provider.SyncStateContract.Helpers.insert
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.Movie
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem

class MoviesRepository(private val movieDao: MovieDao) {

    val allMovies = movieDao.retrieveAllMovies()

    suspend fun insert(movieItem: Movie){
        movieDao.insert(movieItem)
    }

    suspend fun delete(movieItem: Movie){
        movieDao.delete(movieItem)
    }
}