package com.arctouch.codechallenge.home

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arctouch.codechallenge.BuildConfig
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.home.fragment.SearchMovieListFragment
import com.arctouch.codechallenge.home.fragment.UpcomingMovieListFragment
import kotlinx.android.synthetic.main.home_activity.*
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_FRAGMENT = "SELECTED_FRAGMENT"
    }

    private val viewModel : HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    var selectedFragment : Int? = null

    private val upcominghMovieFragment by lazy { UpcomingMovieListFragment.newInstance() }
    private val searchMovieFragment by lazy { SearchMovieListFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setContentView(R.layout.home_activity)


        selectedFragment = savedInstanceState?.getInt(SELECTED_FRAGMENT)

        viewModel.isGenresLoadedLiveData.observe(this, Observer {isGenresLoaded ->
            if (isGenresLoaded) {
                setFragmentFromId(selectedFragment ?: R.id.action_upcoming)
                this.setupBottomNavigation()

                frameLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                tvErrorMessage.visibility = View.GONE
            } else {
                frameLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                tvErrorMessage.visibility = View.VISIBLE
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            this.setFragmentFromId(it.itemId)
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        selectedFragment?.let {  outState?.putInt(SELECTED_FRAGMENT, it) }
    }

    private fun setFragmentFromId(fragmentId : Int) {
        when (fragmentId) {
            R.id.action_upcoming -> {
                replaceFragment(upcominghMovieFragment)
                selectedFragment = R.id.action_upcoming
            }
            R.id.action_search -> {
                replaceFragment(searchMovieFragment)
                selectedFragment = R.id.action_search
            }
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment, fragment.javaClass.simpleName)
            .commit()
    }
}
