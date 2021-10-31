package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieDO
import kotlinx.android.synthetic.main.activity_fav_movie_list.*
import kotlinx.android.synthetic.main.activity_view_list_of_movies.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FavMovieList : AppCompatActivity() {

    var activityCoroutineScope: CoroutineScope? = null

    var dynamoDBMapper: DynamoDBMapper? = null

    var currentMovieDO: MovieDO? = null

    var MoviesList =  mutableListOf<String>()

    var MoviePicList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_movie_list)

        //actionbar
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        activityCoroutineScope?.launch() {

            //TODO 13 :Initialize DynamoDBMapper
            //Step 25
            try {
                var credentials: AWSCredentials = AWSMobileClient.getInstance().awsCredentials
                var dynamoDBClient = AmazonDynamoDBClient(credentials)
                dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(
                        AWSMobileClient.getInstance().configuration
                    )
                    .build()
                runFavMovie()
            } catch (ex: Exception) {

                Log.d("DynamoDBLab", "Exception ${ex.message}")

            }

        }
    }

    fun runFavMovie() {

        activityCoroutineScope?.launch() {
            //TODO 14 :Retrieve any existing data from DynamoDBMapper table
            //Step 27
            //TODO 14.1 :Create the scan expression to retrieve only data created by the user
            var eav = HashMap<String, AttributeValue>()
            eav.put(":val1", AttributeValue().withS(AWSMobileClient.getInstance().username))
            var queryExpression =
                DynamoDBScanExpression().withFilterExpression("id = :val1")
                    .withExpressionAttributeValues(eav)
            var itemList =
                dynamoDBMapper?.scan(MovieDO::class.java, queryExpression)

            //For each item retrieved, assign the data to a variable in the activity.
            // Create a loop to printout the data of each note into a string.
            // If no item exist, create a new MovieDO
            if (itemList?.size != 0 && itemList != null) {

                for (i in itemList!!.iterator()) {

                    currentMovieDO = i

                }

                for (movie in currentMovieDO?.DOmovieList!!.iterator()) {

                    MoviesList.add("${movie.originaltitle}")
                    MoviePicList.add(NetworkUtils.buildImageUrl(movie.posterpath).toString())

                }

                withContext(Dispatchers.Main) {
                    var MoviesAdapter = ViewListOfMoviesActivity.CustListAdapter(
                        this@FavMovieList, MoviesList as ArrayList<String>,
                        MoviePicList as ArrayList<String>
                    )
                    Favmovielist.adapter = MoviesAdapter
                }
            }
            else{
                currentMovieDO = MovieDO()

                currentMovieDO?.apply {

                    id = AWSMobileClient.getInstance().username

                    DOmovieList = mutableListOf()

                }
            }

        }

    }

    //Return to Movie List activity when back button pressed
    override fun onSupportNavigateUp(): Boolean {
        var ReturntoMovieList = Intent(this, ViewListOfMoviesActivity::class.java)
        startActivity(ReturntoMovieList)
        return super.onSupportNavigateUp()
    }
}