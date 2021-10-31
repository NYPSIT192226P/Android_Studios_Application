package com.nyp.sit.movieviewer_intermediate_starter_project

import androidx.lifecycle.*
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.Movie
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(val  repo: MoviesRepository) : ViewModel() {
    val allMovies: LiveData<List<Movie>> = repo.allMovies.asLiveData()

    fun insert(movieItem: Movie) = viewModelScope.launch(Dispatchers.IO){
        repo.insert(movieItem)
    }

    fun remove(contactItem: Movie) = viewModelScope.launch(Dispatchers.IO){
        repo.delete(contactItem)
    }
}

class MoviesViewModelFactory(private val repo : MoviesRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {

            return MoviesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}
