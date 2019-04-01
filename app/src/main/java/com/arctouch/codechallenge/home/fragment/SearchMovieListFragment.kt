package com.arctouch.codechallenge.home.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.home.HomeFragment
import com.arctouch.codechallenge.util.State
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.jetbrains.anko.longToast

/**
 * Fragment that allows the user to search for a movie
 */
class SearchMovieListFragment : HomeFragment() {

    private lateinit var movieListAdapter : HomeAdapter

    companion object {
        fun newInstance(): SearchMovieListFragment {
            return SearchMovieListFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()

        movieListAdapter = HomeAdapter ({viewModel?.retrySearch() }, this::onMovieClick)
        recyclerView.adapter = movieListAdapter

        viewModel?.searchMovieList?.observe(this, Observer {
            movieListAdapter.submitList(it)
        })

        btClear.setOnClickListener{ etSearch.setText("") }
    }

    private fun setupSearch() {
        etSearch.visibility = View.VISIBLE

        etSearch.addTextChangedListener { text ->
            if (text?.length ?: 0 > 0)  {
                btClear.visibility = View.VISIBLE
            } else {
                btClear.visibility = View.GONE
            }
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etSearch.text.toString().length < 3) {
                    activity?.longToast(getString(R.string.search_text_too_short))
                } else {
                    viewModel?.setSearchTextView(etSearch.text.toString())

                    viewModel?.getStateSearch()?.observe(this, Observer { state ->
                        if (viewModel != null && !viewModel!!.isSearchListEmpty()) {
                            movieListAdapter.setState(state ?: State.DONE)
                        } else {

                        }
                    })
                }
                true
            } else {
                false
            }
        }
    }


}
