package com.koresuniku.wishmaster.http

import com.google.gson.GsonBuilder
import com.koresuniku.wishmaster.http.boards_api.BoardsApiService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpClient {

    val client = OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.SECONDS)
            //.proxy(setProxy())
            .readTimeout(10000, TimeUnit.SECONDS).build()!!

    val gson = GsonBuilder().create()!!

    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Dvach.DVACH_BASE_URL)
            .client(client)
            .build()!!

    val boardsService = retrofit.create(BoardsApiService::class.java)!!
    //val threadsService = retrofit.create(ThreadsApiService::class.java)!!
    //val singleThreadService = retrofit.create(SingleThreadApiService::class.java)!!

    fun getResponseFromUrl(url: String): ResponseBody? {
        val request = Request.Builder().url(url).build()
        val response = OkHttpClient().newCall(request).execute()
        val body = response.body()
        return body
    }

}