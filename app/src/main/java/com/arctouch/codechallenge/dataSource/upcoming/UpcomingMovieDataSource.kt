package com.arctouch.codechallenge.dataSource.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class UpcomingMovieDataSource(private val tmdbApi: TmdbApi,
                              private val compositeDisposable: CompositeDisposable
)
    : PageKeyedDataSource<Int, Movie>() {

    var state : MutableLiveData<State> = MutableLiveData()
    private var retryCompletable : Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            tmdbApi.upcomingMovies(1, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                            response ->
                        updateState(State.DONE)
                        callback.onResult(response.results, null, 2)
                    },
                    {
//                        Timber.e(it)
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            tmdbApi.upcomingMovies(params.key.toLong(), TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                            response ->
                        updateState(State.DONE)
                        callback.onResult(response.results, params.key + 1)
                    },
                    {
//                        Timber.e(it)
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }


}