package com.arctouch.codechallenge.di.module

import android.app.Application
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.api.TmdbApi.Companion.API_KEY
import com.arctouch.codechallenge.api.TmdbApi.Companion.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.api.TmdbApi.Companion.URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCache(application: Application) : Cache {
        val cacheSize = (1024 * 1024 * 10).toLong() // 10MB
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideInterceptor() : Interceptor {
        return Interceptor {chain ->
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url()
            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .addQueryParameter("language", DEFAULT_LANGUAGE)
                    .build()

            val requestBuilder = originalRequest.newBuilder().url(url)
            val request = requestBuilder.build()

            chain.proceed(request)
        }
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOkHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)
        client.addInterceptor(interceptor)
        return client.build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideMoshiClient(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRxAdapterClient(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient,
                                          moshiConverterFactory: MoshiConverterFactory,
                                          rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideTmdbApi(retrofit: Retrofit): TmdbApi {
        return retrofit.create(TmdbApi::class.java)
    }
}