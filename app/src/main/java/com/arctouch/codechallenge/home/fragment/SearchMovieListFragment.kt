package com.arctouch.codechallenge.home.fragment

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.home.HomeAdapter
import com.arctouch.codechallenge.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.jetbrains.anko.longToast

/**
 * Fragment that allows the user to search for a movie
 */
class SearchMovieListFragment : HomeFragment() {

    companion object {
        fun newInstance(): SearchMovieListFragment {
            return SearchMovieListFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        setupSearch()

        val movieListAdapter = HomeAdapter ({viewModel?.retry() }, this::onMovieClick)
        recyclerView.adapter = movieListAdapter

        viewModel?.searchMovieList?.observe(this, Observer {
            movieListAdapter.submitList(it)
        })

//        viewModel.searchTextList.observe(this, Observer { etSearch.setText(it) })
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

        etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etSearch.text.toString().length < 3) {
                    activity?.longToast(getString(R.string.search_text_too_short))
                } else {
                    viewModel?.setSearchTextView(etSearch.text.toString())
//                    changeViewType(ViewUtil.Type.PROGRESSBAR)
                }
                true
            } else {
                false
            }
        }
    }


}
