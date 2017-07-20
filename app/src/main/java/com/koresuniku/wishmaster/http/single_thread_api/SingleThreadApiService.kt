package com.koresuniku.wishmaster.http.single_thread_api

import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SingleThreadApiService {

    @GET("/makaba/mobile.fcgi")
    fun getPosts(@Query("task") task: String,
                          @Query("board") board: String,
                          @Query("thread") thread: String,
                          @Query("post") post: Int): Call<List<Post>>
}