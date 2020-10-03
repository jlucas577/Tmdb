package org.themoviedb.joaomartins.ui.activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import org.themoviedb.joaomartins.R
import org.themoviedb.joaomartins.config.Tmdb
import android.view.MenuItem
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.movies_item.*
import org.themoviedb.joaomartins.databases.DbHelper
import org.themoviedb.joaomartins.models.Movies


class MovieActivity : AppCompatActivity() {

    /*
        Widgets
    */
    @BindView(R.id.page_toolbar)
    lateinit var pageToolbar: Toolbar

    @BindView(R.id.content_image)
    lateinit var contentImage: ImageView

    @BindView(R.id.content_poster)
    lateinit var contentPoster: ImageView

    @BindView(R.id.content_title)
    lateinit var contentTitle: TextView

    @BindView(R.id.content_overview)
    lateinit var contentOverview: TextView

    @BindView(R.id.button_favorites_add)
    lateinit var favoritesAdd: Button

    @BindView(R.id.button_favorites_remove)
    lateinit var favoritesRemove: Button


    /*
        Banco de dados
    */
    lateinit var db: DbHelper


    /*
       Dados
    */
    var movieId : String = ""
    var movieTitle : String = ""
    var movieOverview : String = ""
    var moviePosterPath : String = ""
    var movieBackdropPath: String = ""
    var movieAdult: Boolean = false


    /*
        Layout
    */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        ButterKnife.bind(this)

        db = DbHelper(this)

        if (intent.extras != null) {

            movieId = intent.getStringExtra("movieId") ?: "0"
            movieTitle = intent.getStringExtra("movieTitle") ?: " "
            movieOverview = intent.getStringExtra("movieOverview") ?: " "
            moviePosterPath = intent.getStringExtra("moviePosterPath") ?: " "
            movieBackdropPath = intent.getStringExtra("movieBackdropPath") ?: " "
            movieAdult = intent.getBooleanExtra("movieAdult", false)

        }


        contentTitle.text = movieTitle
        contentOverview.text = movieOverview


        setSupportActionBar(pageToolbar)

        supportActionBar?.apply {
            title = movieTitle

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }


        val picasso = Picasso.get()

        picasso.load(Tmdb.tmdbPosterPath + movieBackdropPath)
            .into(contentImage)

        picasso.load(Tmdb.tmdbPosterPath + moviePosterPath)
            .into(contentPoster)


        favoritesAdd.setOnClickListener {
            addFavorites()
        }

        favoritesRemove.setOnClickListener {
            removeFavorites()
        }


        if (::db.isInitialized) {
            loadFavorites()
        }

    }


    /*
        Funções
    */
    private fun loadFavorites() {

        val haveFavorite = db.getMovie(
            movieId.toInt()
        )

        if (haveFavorite == 0) {

            favoritesAdd.visibility = View.VISIBLE
            favoritesRemove.visibility = View.GONE

        } else {

            favoritesAdd.visibility = View.GONE
            favoritesRemove.visibility = View.VISIBLE

        }

    }

    private fun addFavorites() {

        val movie = Movies(
            poster_path = moviePosterPath
            , id = movieId.toInt()
            , adult = movieAdult
            , backdrop_path = movieBackdropPath
            , title = movieTitle
            , overview = movieOverview
        )

        db.newMovies(
            movie
        )

        loadFavorites()

    }

    private fun removeFavorites() {

        val movie = Movies(
            poster_path = moviePosterPath
            , id = movieId.toInt()
            , adult = movieAdult
            , backdrop_path = movieBackdropPath
            , title = movieTitle
            , overview = movieOverview
        )

        db.deleteMovies(
            movie
        )

        loadFavorites()

    }



    /*
        Ciclos de vida
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}