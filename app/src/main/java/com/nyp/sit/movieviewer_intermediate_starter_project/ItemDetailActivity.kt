package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieDO
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_view_list_of_movies.*
import kotlinx.coroutines.*
import java.lang.Exception

class ItemDetailActivity : AppCompatActivity() {

    var activityCoroutineScope: CoroutineScope? = null

    var dynamoDBMapper: DynamoDBMapper? = null

    var currentMovieDO: MovieDO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

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

        //actionbar
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        var moviePosterPath = intent.getStringExtra("poster_path")
        var movieOverview = intent.getStringExtra("overview")
        var movieReleseDate = intent.getStringExtra("releaseDate")
        var moviePopularity = intent.getStringExtra("popularity")
        var movieVoteCount = intent.getStringExtra("voteCount")
        var movieVoteAverage = intent.getStringExtra("voteAverage")
        var movieLanguage = intent.getStringExtra("language")
        var movieAdult = intent.getStringExtra("adult")
        var movieVideo = intent.getStringExtra("hasVideo")

        Picasso.get().load(NetworkUtils.buildImageUrl(moviePosterPath).toString()).into(posterIV)
        movie_overview.text = movieOverview
        movie_release_date.text = movieReleseDate
        movie_popularity.text = moviePopularity
        movie_vote_count.text = movieVoteCount
        movie_vote_avg.text = movieVoteAverage
        movie_langauge.text = movieLanguage
        movie_is_adult.text = movieAdult
        movie_hasvideo.text = movieVideo

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itemdetailmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            R.id.AddFav -> {
                //Add new note to array list
                var moviePosterPath = intent.getStringExtra("poster_path")
                var movieOverview = intent.getStringExtra("overview")
                var movieReleseDate = intent.getStringExtra("releaseDate")
                var moviePopularity = intent.getStringExtra("popularity")
                var movieVoteCount = intent.getStringExtra("voteCount")
                var movieVoteAverage = intent.getStringExtra("voteAverage")
                var movieLanguage = intent.getStringExtra("language")
                var movieAdult = intent.getStringExtra("adult")
                var movieVideo = intent.getStringExtra("hasVideo")
                var moviebackdrop = intent.getStringExtra("backdropPath")
                var movieGenreid = intent.getStringExtra("genreid")
                var movieOriginalTitle = intent.getStringExtra("original_title")
                var movieId = intent.getStringExtra("movieid")
                var movieTitle = intent.getStringExtra("title")
                var newMovie = MovieDO.Movie()

                newMovie.posterpath = moviePosterPath

                newMovie.overview = movieOverview

                newMovie.releasedate = movieReleseDate

                newMovie.popularity = moviePopularity

                newMovie.votecount = movieVoteCount

                newMovie.voteaverage = movieVoteAverage

                newMovie.originallanguage = movieLanguage

                newMovie.adult = movieAdult

                newMovie.video = movieVideo

                newMovie.backdroppath = moviebackdrop

                newMovie.genreid = movieGenreid

                newMovie.originaltitle = movieOriginalTitle

                newMovie.movidId = movieId

                newMovie.title = movieTitle

                currentMovieDO?.DOmovieList?.add(newMovie)


                //TODO 16 : Make use of DynamoDBMapper to save NotesDO to DynamoDB table.
                //Step 31
                activityCoroutineScope?.launch() {

                    dynamoDBMapper?.save((currentMovieDO))

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Return to Movie List activity when back button pressed
    override fun onSupportNavigateUp(): Boolean {
        var ReturntoMovieList = Intent(this, ViewListOfMoviesActivity::class.java)
        startActivity(ReturntoMovieList)
        return super.onSupportNavigateUp()
    }

    fun runFavMovie() {

        activityCoroutineScope?.launch() {
            var MoviesList =  mutableListOf<String>()

            var MoviePicList = mutableListOf<String>()
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

            //TODO 14.2 :For each item retrieved, assign the data to a variable in the activity.
            // Create a loop to printout the data of each note into a string.
            // If no item exist, create a new NotesDO
            //Step 29
            if (itemList?.size != 0 && itemList != null) {

                for (i in itemList!!.iterator()) {

                    currentMovieDO = i

                }

                for (movie in currentMovieDO?.DOmovieList!!.iterator()) {

                    MoviesList.add("${movie.originaltitle}")
                    MoviePicList.add(NetworkUtils.buildImageUrl(movie.posterpath).toString())

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
}
