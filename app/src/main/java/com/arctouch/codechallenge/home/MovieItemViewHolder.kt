package com.arctouch.codechallenge.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.setPosterUrl
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    fun bind(movie: Movie?,
             listener: (Movie) -> Unit) = with(itemView) {
        movie?.let {
            titleTextView.text = movie.title
            genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            releaseDateTextView.text = movie.releaseDate

            posterImageView.setPosterUrl(this.context, movie.posterPath)

        setOnClickListener{ listener(movie) }
        }
    }

    companion object {
        fun create(parent: ViewGroup): MovieItemViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item, parent, false)
            return MovieItemViewHolder(view)
        }
    }
}