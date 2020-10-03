package org.themoviedb.joaomartins.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movies_item.view.*
import org.themoviedb.joaomartins.R
import org.themoviedb.joaomartins.models.Movies
import org.themoviedb.joaomartins.config.Tmdb
import org.themoviedb.joaomartins.ui.activitys.MovieActivity

class MoviesAdapters(private val movies: List<Movies>) : RecyclerView.Adapter<MoviesAdapters.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movies_item, parent, false
        )

        return ViewHolder(
            view
        )

    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val movie = movies[position]
        val picasso = Picasso.get()

        picasso.load(Tmdb.tmdbPosterPath + movie.poster_path)
            .into(holder.posterPathBg)

        holder.adult.text = movie.adult.toString()
        holder.title.text = movie.title


        holder.itemView.setOnClickListener {

            val activity = holder.itemView.context as Activity
            val intent = Intent(activity, MovieActivity::class.java).apply {
                putExtra("movieId", movie.id.toString())
                putExtra("movieTitle", movie.title)
                putExtra("movieOverview", movie.overview)
                putExtra("moviePosterPath", movie.poster_path)
                putExtra("movieBackdropPath", movie.backdrop_path)
                putExtra("movieAdult", movie.adult)
            }

            activity.startActivity(intent)

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val posterPathBg : ImageView = itemView.posterPath
        val adult : TextView = itemView.adult
        val title : TextView = itemView.title

    }

}