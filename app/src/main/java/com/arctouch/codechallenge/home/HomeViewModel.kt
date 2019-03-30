package com.arctouch.codechallenge.home

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseViewModel
import com.arctouch.codechallenge.data.Cache
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

    init {
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
}
