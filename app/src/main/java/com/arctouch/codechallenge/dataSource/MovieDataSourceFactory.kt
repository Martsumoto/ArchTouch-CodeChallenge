package com.arctouch.codechallenge.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                             private val tmdbApi: TmdbApi)
    : DataSource.Factory<Int, Movie>() {

    val movieDataSourceLiveData = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(tmdbApi, compositeDisposable)
        movieDataSourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }
}