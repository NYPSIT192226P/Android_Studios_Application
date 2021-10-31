package com.nyp.sit.movieviewer_intermediate_starter_project

import android.content.Context
import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.SignOutOptions
import com.nyp.sit.movieviewer_intermediate_starter_project.entity.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_view_list_of_movies.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception

class ViewListOfMoviesActivity : AppCompatActivity() {


    val SHOW_BY_TOP_RATED = 1
    val SHOW_BY_POPULAR = 2
    var myMovies: MyMovies ?= MyMovies().getInstance()

    private var displayType = SHOW_BY_TOP_RATED

    //var MoviesAdapter: ArrayAdapter<String>? = null
    var MoviesAdapter: CustListAdapter? = null
    var allMovies: List<com.nyp.sit.movieviewer_intermediate_starter_project.entity.Movie>? = null

    private val moviesViewModel: MoviesViewModel by viewModels() {
        MoviesViewModelFactory((application as MyMovies).repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list_of_movies)
        movielist.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var movieDetailIntent = Intent(this@ViewListOfMoviesActivity, ItemDetailActivity::class.java)
                movieDetailIntent.putExtra("poster_path", allMovies?.get(p2)?.poster_path.toString())
                movieDetailIntent.putExtra("overview", allMovies?.get(p2)?.overview.toString())
                movieDetailIntent.putExtra("releaseDate", allMovies?.get(p2)?.release_date.toString())
                movieDetailIntent.putExtra("popularity", allMovies?.get(p2)?.popularity.toString())
                movieDetailIntent.putExtra("voteCount", allMovies?.get(p2)?.vote_count.toString())
                movieDetailIntent.putExtra("voteAverage", allMovies?.get(p2)?.vote_average.toString())
                movieDetailIntent.putExtra("language", allMovies?.get(p2)?.original_langauge.toString())
                movieDetailIntent.putExtra("adult", allMovies?.get(p2)?.adult.toString())
                movieDetailIntent.putExtra("hasVideo", allMovies?.get(p2)?.video.toString())
                movieDetailIntent.putExtra("backdropPath", allMovies?.get(p2)?.backdrop_path.toString())
                movieDetailIntent.putExtra("genreid", allMovies?.get(p2)?.genre_ids.toString())
                movieDetailIntent.putExtra("original_title", allMovies?.get(p2)?.original_title.toString())
                movieDetailIntent.putExtra("movieid", allMovies?.get(p2)?.id.toString())
                movieDetailIntent.putExtra("title", allMovies?.get(p2)?.title.toString())
                startActivity(movieDetailIntent)
            }
        }

        moviesViewModel.allMovies.observe(this, Observer {
            var moviesConvertList = mutableListOf<String>()
            var MoviePicList = mutableListOf<String>()
            allMovies = it
            for (movie in it){
                moviesConvertList.add("${movie.original_title}")
                MoviePicList.add(NetworkUtils.buildImageUrl(movie.poster_path).toString())
            }
            it?.let {
                //MoviesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, moviesConvertList)
                MoviesAdapter = CustListAdapter(this, moviesConvertList as ArrayList<String>,
                    MoviePicList as ArrayList<String>
                )
                movielist.adapter = MoviesAdapter
                if (myMovies?.MovieListState != null) {
                    movielist.onRestoreInstanceState(myMovies?.MovieListState)
                }
            }
            toggleVisibility()
        })

    }
    private fun toggleVisibility() {
        if (movielist.count > 0) {
            movielist.visibility = View.VISIBLE
        } else {
            movielist.visibility = View.GONE
        }
    }


    override fun onStart() {
        super.onStart()
        displayType = myMovies!!.displayType
        loadMovieData(displayType)
    }


    fun loadMovieData(viewType: Int) {

        var showTypeStr: String? = null

        when (viewType) {
            SHOW_BY_TOP_RATED -> showTypeStr = NetworkUtils.TOP_RATED_PARAM
            SHOW_BY_POPULAR -> showTypeStr = NetworkUtils.POPULAR_PARAM

        }

        if (showTypeStr != null) {
            displayType = viewType
            var responseList = ArrayList<MovieItem>()
            var MovieListJob = CoroutineScope(Job() + Dispatchers.IO).async {
                val MovieListRequestUrl = NetworkUtils.buildUrl(showTypeStr,getString(R.string.moviedb_api_key))
                val jsonMovieListResponse = NetworkUtils
                        .getResponseFromHttpUrl(MovieListRequestUrl!!)

                jsonMovieListResponse
            }
            //TODO 5 : Create a coroutine using the main dispatcher
            //TODO 6 : Wait for the response from the job created previously
            //TODO 7 : Make use of OpenWeatherUtils to breakdown the JSON response
            GlobalScope.launch(Dispatchers.Main) {
                var response = MovieListJob.await()
                var movieData = JSONObject(response)
                responseList = movieDBJsonUtils
                        .getMovieDetailsFromJson(this@ViewListOfMoviesActivity,
                                movieData.toString())!!
                withContext(Dispatchers.Main) {
                    // delete all entries before inserting
                    if (allMovies != null) {
                        for (i in 0 until allMovies!!.size)
                        {
                            moviesViewModel.remove(allMovies!![i])
                        }
                    }

                    // insert
                    for (i in 0 until responseList.size)
                    {
                        Log.d("MovieItems",responseList[i].original_langauge)
                        var poster_path = responseList[i].poster_path
                        var adult = responseList[i].adult
                        var overview = responseList[i].overview
                        var release_date = responseList[i].release_date
                        var genre_ids = responseList[i].genre_ids
                        var original_title = responseList[i].original_title
                        var original_langauge = responseList[i].original_langauge
                        var title = responseList[i].title
                        var backdrop_path = responseList[i].backdrop_path
                        var popularity = responseList[i].popularity
                        var vote_count = responseList[i].vote_count
                        var video = responseList[i].video
                        var vote_average = responseList[i].vote_average
                        var MovieObject = com.nyp.sit.movieviewer_intermediate_starter_project.entity.Movie(i,poster_path.toString(),adult.toString(),overview.toString(),release_date.toString(),genre_ids.toString(),original_title.toString(),original_langauge.toString(),title.toString(),backdrop_path.toString(),popularity.toString(),vote_count.toString(),video.toString(),vote_average.toString())
                        moviesViewModel.insert(MovieObject)
                    }
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.sortPopular -> {
                loadMovieData(SHOW_BY_POPULAR)
            }
            R.id.sortTopRated -> {
                loadMovieData(SHOW_BY_TOP_RATED)
            }
            R.id.viewFav -> {
                var FavMovieListIntent = Intent(this, FavMovieList::class.java)
                startActivity(FavMovieListIntent)
            }
            R.id.signout -> {
                //Signout from Cognito and redirect the user back to login screen.
                AWSMobileClient.getInstance()
                    .signOut(
                        SignOutOptions.builder().signOutGlobally(false).build(),

                        object : Callback<Void> {
                            override fun onResult(result: Void?) {
                                val signoutIntent = Intent(applicationContext, Login::class.java)

                                startActivity(signoutIntent)

                                finish()
                            }

                            override fun onError(e: Exception?) {

                            }
                        }
                    )
            }

        }

        return super.onOptionsItemSelected(item)
    }
    class CustListAdapter(context: Context, data:ArrayList<String>, PicData: ArrayList<String>): BaseAdapter() {

        internal val sList: ArrayList<String>? = ArrayList<String>()
        internal val picList: ArrayList<String>? = ArrayList<String>()
        //TODO 3.2: Uncomment away the below statement

        private val mInflater: LayoutInflater

        init {
            this.mInflater = LayoutInflater.from(context)
            sList?.addAll(data)
            picList?.addAll(PicData)
        }

        fun addAll(data: Collection<String>) {

            sList?.addAll(data)

        }

        fun addAllPic(data: Collection<String>) {

            picList?.addAll(data)

        }

        fun clear() {

            sList?.clear()

        }

        override fun getItem(p0: Int): Any? {

            return sList?.get(p0)
        }

        override fun getItemId(p0: Int): Long {

            return 0
        }

        override fun getCount(): Int {
            return if(sList == null) 0 else sList?.size
        }


        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val v: View
            v = this.mInflater.inflate(R.layout.item_list, p2, false)
            val label: TextView = v.findViewById(R.id.movieTitle)
            label.text = sList?.get(p0)
            val iv: ImageView = v.findViewById(R.id.Movieimage)

            Picasso.get().load(picList?.get(p0)).into(iv)

            return v
        }
    }
    override fun onPause() {
        super.onPause()
        myMovies?.MovieListState = movielist.onSaveInstanceState()
        myMovies?.displayType = displayType
    }
}
