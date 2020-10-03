package org.themoviedb.joaomartins.requests

import org.themoviedb.joaomartins.models.Movies

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET("/3/movie/now_playing?api_key=d894a441bacf1509fd4a16155bc31b97&language=pt-BR&page=1")
    fun getNowPlaying(): Call<List<Movies>>

    @GET
    fun getSearch(@Url url: String): Call<List<Movies>>

}