package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.Movie
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(Movie::class), version = 2, exportSchema = false)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun MovieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null
        fun getDatabase(context: Context, scope:CoroutineScope) : MovieRoomDatabase {
            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(context, MovieRoomDatabase::class.java, "movies_database").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }

}