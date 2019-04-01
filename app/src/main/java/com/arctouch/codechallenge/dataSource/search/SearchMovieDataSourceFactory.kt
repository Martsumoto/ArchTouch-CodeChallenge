package com.arctouch.codechallenge.dataSource.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.CompositeDisposable

class SearchMovieDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                                   private val tmdbApi: TmdbApi,
                                   private val textSearch : String)
    : DataSource.Factory<Int, Movie>() {

    val movieDataSourceLiveData = MutableLiveData<SearchMovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource =
            SearchMovieDataSource(tmdbApi, compositeDisposable, textSearch)
        movieDataSourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }
}