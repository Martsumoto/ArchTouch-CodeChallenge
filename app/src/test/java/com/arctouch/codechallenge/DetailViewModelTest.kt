package com.arctouch.codechallenge

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.detail.DetailViewModel
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.buildResponse
import com.arctouch.codechallenge.util.immediate
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import retrofit2.HttpException


@Suppress("UNCHECKED_CAST")
class DetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var tmdbApi: TmdbApi

    @Mock
    lateinit var application: Application

    lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

        detailViewModel = DetailViewModel(application)
        detailViewModel.tmdbApi = tmdbApi
    }

    @Test
    fun `test call show movie detail`() {
        val movie = Movie(123, "title", "overview", listOf(), listOf(), "", "", "")

        val movieObserver = Mockito.mock(Observer::class.java) as Observer<Movie>
        val errorObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        detailViewModel.movieDetailLiveData.observeForever(movieObserver)
        detailViewModel.isLoadErrorLiveData.observeForever(errorObserver)
        Mockito.`when`(tmdbApi.movie(123)).thenReturn(Observable.just(movie))
        detailViewModel.loadMovie(123)

        Assert.assertNotNull(detailViewModel.movieDetailLiveData.value)
        Assert.assertFalse(detailViewModel.isLoadErrorLiveData.value?:false)

    }

    @Test
    fun `test load movie detail error`() {
        val observer = Mockito.mock(Observer::class.java) as Observer<Boolean>
        detailViewModel.isLoadErrorLiveData.observeForever(observer)
        Mockito.`when`(tmdbApi.movie(123)).thenReturn(Observable.error(HttpException(buildResponse())))
        detailViewModel.loadMovie(123)

        Assert.assertTrue(detailViewModel.isLoadErrorLiveData.value?:false)

    }

}