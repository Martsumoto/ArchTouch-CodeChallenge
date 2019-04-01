package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.detail.DetailActivity
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity() {

    private val viewModel : HomeViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(this.application).create(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val movieListAdapter = HomeAdapter ({viewModel.retry() }, this::onMovieClick)
        recyclerView.adapter = movieListAdapter

        viewModel.movieList.observe(this, Observer {
            movieListAdapter.submitList(it)
        })
    }

    private fun onMovieClick(movie: Movie) {
        startDetailActivity(movie)
    }

    private fun startDetailActivity(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.MOVIE_ID, movie.id)
        intent.putExtra(DetailActivity.MOVIE_NAME, movie.title)
        startActivity(intent)
    }
}
