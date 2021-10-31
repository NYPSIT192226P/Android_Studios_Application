package com.nyp.sit.movieviewer_intermediate_starter_project.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="movielist_table")
data class Movie (@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id : Int,

                  @ColumnInfo(name = "poster_path") val poster_path: String,

                  @ColumnInfo(name = "adult") val adult : String,

                  @ColumnInfo(name = "overview") val overview: String,

                  @ColumnInfo(name = "release_date") val release_date : String,

                  @ColumnInfo(name = "genre_ids") val genre_ids: String,

                  @ColumnInfo(name = "original_title") val original_title : String,

                  @ColumnInfo(name = "original_langauge") val original_langauge : String,

                  @ColumnInfo(name = "title") val title : String,

                  @ColumnInfo(name = "backdrop_path") val backdrop_path : String,

                  @ColumnInfo(name = "popularity") val popularity : String,

                  @ColumnInfo(name = "vote_count") val vote_count : String,

                  @ColumnInfo(name = "video") val video : String,

                  @ColumnInfo(name = "vote_average") val vote_average : String)
