package com.arctouch.codechallenge.di.component

import android.app.Application
import com.arctouch.codechallenge.detail.DetailViewModel
import com.arctouch.codechallenge.di.module.NetworkModule
import com.arctouch.codechallenge.home.HomeViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class
])
interface ViewModelInjector {
    fun inject(homeViewModel: HomeViewModel)
    fun inject(detailViewModel: DetailViewModel)

    @Component.Builder
    interface Builder {
        fun build() : ViewModelInjector

        fun networkModule(networkModule : NetworkModule) : Builder

        @BindsInstance
        fun application(application: Application) : Builder
    }
}