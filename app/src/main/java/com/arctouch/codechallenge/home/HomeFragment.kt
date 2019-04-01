package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.detail.DetailActivity
import com.arctouch.codechallenge.model.Movie

abstract class HomeFragment : Fragment() {
    companion object {
        const val UPCOMING_MOVIES = "UpcomingMovies"
        const val SEARCH_MOVIES = "SearchMovies"
    }


    protected val viewModel by lazy {
        activity?.let {
            ViewModelProviders.of(it).get(HomeViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        return view
    }

    protected fun onMovieClick(movie: Movie) {
        startDetailActivity(movie)
    }

    private fun startDetailActivity(movie: Movie) {
        val intent = Intent(this.activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.MOVIE_ID, movie.id)
        intent.putExtra(DetailActivity.MOVIE_NAME, movie.title)
        startActivity(intent)
    }
}