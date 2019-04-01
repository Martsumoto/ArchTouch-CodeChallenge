package com.arctouch.codechallenge.home.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_movie_list.*

/**
 * Fragment that allows the user to search for a movie
 */
class UpcomingMovieListFragment : HomeFragment() {

    companion object {
        fun newInstance(): UpcomingMovieListFragment {
            return UpcomingMovieListFragment()
        }
    }

    override fun onResume() {
        super.onResume()

        etSearch.visibility = View.GONE
        btClear.visibility = View.GONE
        val movieListAdapter = HomeAdapter ({viewModel?.retry() }, this::onMovieClick)
        recyclerView.adapter = movieListAdapter

        viewModel?.upcomingMovieList?.observe(this, Observer {
            movieListAdapter.submitList(it)
        })
    }
}
