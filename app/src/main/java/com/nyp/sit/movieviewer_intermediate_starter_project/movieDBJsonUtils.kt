package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Context
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem

import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.util.ArrayList
import kotlin.jvm.Throws

class movieDBJsonUtils() {


    companion object {


        @Throws(JSONException::class)
        fun getMovieDetailsFromJson(context: Context, movieDetailsJsonStr: String): ArrayList<MovieItem>? {

            val ML_PAGE = "page"
            val ML_TOTAL_RESULT = "total_results"
            val ML_TOTAL_PAGES = "total_pages"
            val ML_RESULTS = "results"
            val ML_VOTE_COUNT = "vote_count"
            val ML_ID = "id"
            val ML_VIDEO = "video"
            val ML_VOTE_AVERAGE = "vote_average"
            val ML_TITLE = "title"
            val ML_POPULARITY = "popularity"
            val ML_POSTER_PATH = "poster_path"
            val ML_ORIGINAL_LANGUAGE = "original_language"
            val ML_ORIGINAL_TITLE = "original_title"
            val ML_GENRE_IDS = "genre_ids"
            val ML_BACKDROP_PATH = "backdrop_path"
            val ML_ADULT = "adult"
            val ML_OVERVIEW = "overview"
            val ML_RELEASE_DATE = "release_date"
            val ML_MESSAGE_CODE = "Message_code"

//            val ML_POSTER_PATH = "poster_path"
//            val ML_ADULT = "adult"
//            val ML_OVERVIEW = "overview"
//            val ML_RELEASE_DATE = "release_date"
//            val ML_GENRE_IDS = "genre_ids"
//            val ML_ID = "id"
//            val ML_ORIGINAL_TITLE = "original_title"
//            val ML_ORIGINAL_LANGUAGE = "original_language"
//            val ML_TITLE = "title"
//            val ML_BACKDROP_PATH = "backdrop_path"
//            val ML_POPULARITY = "popularity"
//            val ML_VOTE_COUNT = "vote_count"
//            val ML_VIDEO = "video"
//            val ML_VOTE_AVERAGE = "vote_average"
//
//            val ML_RESULTS = "results"
//
//            val ML_MESSAGE_CODE = "Message_code"
            

            //Array to hold the data for each movie
            val parsedMovieData = ArrayList<MovieItem>()

            val movieDetailsJson = JSONObject(movieDetailsJsonStr)

            //In case of error
            if (movieDetailsJson.has(ML_MESSAGE_CODE)) {
                val errorCode = movieDetailsJson.getInt(ML_MESSAGE_CODE)

                when (errorCode) {
                    HttpURLConnection.HTTP_OK -> {
                    }
                    HttpURLConnection.HTTP_NOT_FOUND ->
                        /*Location invalid*/
                        return null
                    else ->
                        return null
                }
            }


            val MovieDetailArray = movieDetailsJson.getJSONArray(ML_RESULTS)

            for (i in 0 until MovieDetailArray.length()){

                val Poster_path: String
                val Adult : Boolean
                val Overview: String
                val Release_date: String
                val Genre_ids: String
                val Id: Int
                val Original_Title: String
                val Original_Language: String
                val Title: String
                val Backdrop_Path: String
                val Popularity: Double
                val Vote_Count: Int
                val Video: Boolean
                val Vote_Average: Double

                val MovieListDetails = MovieDetailArray.getJSONObject(i)

                Poster_path = MovieListDetails.getString(ML_POSTER_PATH)
                Adult = MovieListDetails.getBoolean(ML_ADULT)
                Overview = MovieListDetails.getString(ML_OVERVIEW)
                Release_date = MovieListDetails.getString(ML_RELEASE_DATE)
                Genre_ids = MovieListDetails.getString(ML_GENRE_IDS)
                Id = MovieListDetails.getInt(ML_ID)
                Original_Title = MovieListDetails.getString(ML_ORIGINAL_TITLE)
                Original_Language = MovieListDetails.getString(ML_ORIGINAL_LANGUAGE)
                Title = MovieListDetails.getString(ML_TITLE)
                Backdrop_Path = MovieListDetails.getString(ML_BACKDROP_PATH)
                Popularity = MovieListDetails.getDouble(ML_POPULARITY)
                Vote_Count = MovieListDetails.getInt(ML_VOTE_COUNT)
                Video = MovieListDetails.getBoolean(ML_VIDEO)
                Vote_Average = MovieListDetails.getDouble(ML_VOTE_AVERAGE)

                parsedMovieData.add(MovieItem(Poster_path, Adult, Overview, Release_date, Genre_ids, Id, Original_Title, Original_Language, Title, Backdrop_Path, Popularity, Vote_Count, Video, Vote_Average))

            }

            return parsedMovieData
        }



    }

}