package com.arctouch.codechallenge.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseViewModel
import com.arctouch.codechallenge.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var tmdbApi: TmdbApi

    private val compDisposable = CompositeDisposable()

    val movieDetailLiveData : MutableLiveData<Movie> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        compDisposable.clear()
    }

    fun loadMovie(id : Int) : LiveData<Movie> {
        compDisposable.add(
                tmdbApi.movie(id.toLong())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            movieDetailLiveData.value = it
                        }
        )

        return movieDetailLiveData
    }
}
