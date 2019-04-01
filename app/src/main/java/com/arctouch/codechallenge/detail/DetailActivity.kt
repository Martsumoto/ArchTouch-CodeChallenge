package com.arctouch.codechallenge.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.setBackdropUrl
import com.arctouch.codechallenge.util.setPosterUrl
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.detail_activity.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID = "MovieId"
        const val MOVIE_NAME = "PosterName"
    }

    private val viewModel : DetailViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(this.application).create(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        this.setupToolbar()

        viewModel.loadMovie(intent.getIntExtra(MOVIE_ID, 0)).observe(this,
            Observer { showDetails(it) })
    }

    fun showProgressBar() {
        content.visibility = View.GONE
//        progressBar.visibility = View.VISIBLE
        tvErrorMessage.visibility = View.GONE
    }

    fun showLoadError(messageId : Int) {
        content.visibility = View.GONE
//        progressBar.visibility = View.GONE
        tvErrorMessage.visibility = View.VISIBLE

        tvErrorMessage.text = getString(messageId)
    }

    private fun showDetails(movie: Movie) {
        tvGenres.text = movie.genres?.joinToString(separator = ", ") { it.name }
        tvOverview.text = movie.overview
        tvReleaseDate.text = movie.releaseDate

        posterImageView.setPosterUrl(this, movie.posterPath)
        ivBackdrop.setBackdropUrl(this, movie.backdropPath)
    }

    private fun setupToolbar() {
        val name = intent.getStringExtra(MOVIE_NAME)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (collapsingToolbar as CollapsingToolbarLayout).title = name ?: ""
    }
}