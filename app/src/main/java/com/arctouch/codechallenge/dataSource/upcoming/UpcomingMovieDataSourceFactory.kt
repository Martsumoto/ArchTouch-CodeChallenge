package com.arctouch.codechallenge.dataSource.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.CompositeDisposable

class UpcomingMovieDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                                     private val tmdbApi: TmdbApi)
    : DataSource.Factory<Int, Movie>() {

    val movieDataSourceLiveData = MutableLiveData<UpcomingMovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource =
            UpcomingMovieDataSource(tmdbApi, compositeDisposable)
        movieDataSourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }
}