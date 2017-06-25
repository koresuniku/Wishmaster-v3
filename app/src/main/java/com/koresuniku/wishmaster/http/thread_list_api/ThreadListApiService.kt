package com.koresuniku.wishmaster.http.thread_list_api

import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListForPagesJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ThreadListApiService {

    @GET("/{id}/catalog.json")
    fun getThreads(@Path("id") boardId: String): Call<ThreadListJsonSchema>

    @GET("/{id}/{page}.json")
    fun getThreadsForPages(@Path("id") boardId: String, @Path("page") page: String): Call<ThreadListForPagesJsonSchema>
}