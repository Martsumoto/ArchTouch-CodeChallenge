package com.arctouch.codechallenge.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity() {

    private val viewModel : HomeViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(this.application).create(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        viewModel.moviesLiveData.observe(this,
                Observer {
                    recyclerView.adapter = HomeAdapter(it ?: emptyList())
                    progressBar.visibility = View.GONE
                })
    }
}
