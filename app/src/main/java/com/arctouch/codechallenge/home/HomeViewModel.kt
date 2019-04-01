package com.arctouch.codechallenge.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseViewModel
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.dataSource.search.SearchMovieDataSourceFactory
import com.arctouch.codechallenge.dataSource.upcoming.UpcomingMovieDataSourceFactory
import com.arctouch.codechallenge.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel(application: Application) : BaseViewModel(application) {

    companion object {
        const val PAGE_SIZE = 10
    }

    @Inject
    lateinit var tmdbApi: TmdbApi

    private val compDisposable = CompositeDisposable()

    val isGenresLoadedLiveData : MutableLiveData<Boolean> = MutableLiveData()

    // Upcoming movies
    private val upcomingMovieDataSourceFactory : UpcomingMovieDataSourceFactory
    var upcomingMovieList : LiveData<PagedList<Movie>>

    // Search movies
    private val searchTextLiveData : MutableLiveData<String> = MutableLiveData()
    var searchMovieList : LiveData<PagedList<Movie>>

    init {
        loadGenres()

        upcomingMovieDataSourceFactory =
            UpcomingMovieDataSourceFactory(compDisposable, tmdbApi)
        val upcomingConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()
        upcomingMovieList = LivePagedListBuilder<Int, Movie>(upcomingMovieDataSourceFactory, upcomingConfig).build()


        searchMovieList = Transformations.switchMap(searchTextLiveData, this::searchMovie)
    }

    private fun searchMovie(textSearch : String) : LiveData<PagedList<Movie>> {
        val searchMovieDataSourceFactory =
            SearchMovieDataSourceFactory(compDisposable, tmdbApi, textSearch)
        val searchConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()
        return LivePagedListBuilder<Int, Movie>(searchMovieDataSourceFactory, searchConfig).build()
    }

    override fun onCleared() {
        super.onCleared()
        compDisposable.clear()
    }

    private fun loadGenres() {
        compDisposable.add(
        tmdbApi.genres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    isGenresLoadedLiveData.value = true
                }
        )
    }

    fun retry() {
        upcomingMovieDataSourceFactory.movieDataSourceLiveData.value?.retry()
    }

    fun setSearchTextView(query: String) {
        searchTextLiveData.value = query
    }
}
