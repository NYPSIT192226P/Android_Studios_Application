package com.nyp.sit.movieviewer_intermediate_starter_project

import android.app.Application
import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyMovies() : Application() {
    val appScope = CoroutineScope(SupervisorJob())
    val db by lazy {MovieRoomDatabase.getDatabase(this, appScope)}
    val repo by lazy {MoviesRepository(db!!.MovieDao())}
    var MovieListState : Parcelable? = null
    var displayType: Int = 1

    companion object{
        private var appInstance = MyMovies()
    }

    fun getInstance():MyMovies
    {
        return appInstance
    }

}