package com.arctouch.codechallenge.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.home.HomeFragment
import com.arctouch.codechallenge.util.State
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch.visibility = View.GONE
        btClear.visibility = View.GONE

        val movieListAdapter = HomeAdapter ({viewModel?.retryUpcoming() }, this::onMovieClick)
        recyclerView.adapter = movieListAdapter

        viewModel?.upcomingMovieList?.observe(this, Observer {
            movieListAdapter.submitList(it)
        })

        viewModel?.getStateUpcoming()?.observe(this, Observer { state ->
            if (viewModel != null && !viewModel!!.isUpcomingListEmpty()) {
                movieListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}
