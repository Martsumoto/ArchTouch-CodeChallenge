package com.arctouch.codechallenge.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseViewModel
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.dataSource.MovieDataSourceFactory
import com.arctouch.codechallenge.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var tmdbApi: TmdbApi

    private val compDisposable = CompositeDisposable()

    val moviesLiveData : MutableLiveData<List<Movie>> = MutableLiveData()

    private val movieDataSourceFactory : MovieDataSourceFactory

    var movieList : LiveData<PagedList<Movie>>

    init {
        movieDataSourceFactory = MovieDataSourceFactory(compDisposable, tmdbApi)
        val config = PagedList.Config.Builder()
            .setPageSize(2)
            .setInitialLoadSizeHint(4)
            .setEnablePlaceholders(false)
            .build()

        movieList = LivePagedListBuilder<Int, Movie>(movieDataSourceFactory, config).build()

        loadGenres()
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
                    loadMovies()
                }
        )
    }

    private fun loadMovies() {
        compDisposable.add(
                tmdbApi.upcomingMovies(1, TmdbApi.DEFAULT_REGION)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val moviesWithGenres = it.results.map { movie ->
                                movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                            }
                            moviesLiveData.value = moviesWithGenres
                        }
        )
    }

    fun retry() {
        movieDataSourceFactory.movieDataSourceLiveData.value?.retry()
    }
}
