package com.arctouch.codechallenge.api

import com.arctouch.codechallenge.model.FetchMoviesResponse
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    companion object {
        const val URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "1f54bd990f1cdfb230adb312546d765d"
        const val DEFAULT_LANGUAGE = "pt-BR"
        const val DEFAULT_REGION = "BR"
    }

    @GET("genre/movie/list")
    fun genres(
    ): Observable<GenreResponse>

    @GET("movie/upcoming")
    fun upcomingMovies(
        @Query("page") page: Long
    ): Observable<FetchMoviesResponse>

    @GET("movie/upcoming")
    fun upcomingMovies(
        @Query("page") page: Long,
        @Query("region") region: String
    ): Observable<FetchMoviesResponse>

    @GET("movie/{id}")
    fun movie(
        @Path("id") id: Long
    ): Observable<Movie>


    @GET("search/movie")
    fun searchMovies(
        @Query("page") page: Long,
        @Query("query") query: String
    ): Observable<FetchMoviesResponse>
}
