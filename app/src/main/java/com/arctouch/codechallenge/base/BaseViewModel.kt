package com.arctouch.codechallenge.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.arctouch.codechallenge.detail.DetailViewModel
import com.arctouch.codechallenge.di.component.DaggerViewModelInjector
import com.arctouch.codechallenge.di.component.ViewModelInjector
import com.arctouch.codechallenge.di.module.NetworkModule
import com.arctouch.codechallenge.home.HomeViewModel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val injector : ViewModelInjector = DaggerViewModelInjector
            .builder()
            .application(application)
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    private fun inject() {
        when(this) {
            is HomeViewModel -> injector.inject(this)
            is DetailViewModel -> injector.inject(this)
        }
    }
}