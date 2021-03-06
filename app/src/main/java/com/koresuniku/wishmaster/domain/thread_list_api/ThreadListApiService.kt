package com.koresuniku.wishmaster.domain.thread_list_api

import com.koresuniku.wishmaster.domain.thread_list_api.model.ThreadListForPagesJsonSchema
import com.koresuniku.wishmaster.domain.thread_list_api.model.ThreadListJsonSchema
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ThreadListApiService {

    @GET("/{id}/catalog.json")
    fun getThreads(@Path("id") boardId: String): Call<ThreadListJsonSchema>

    @GET("/{id}/{page}.json")
    fun getThreadsForPages(@Path("id") boardId: String, @Path("page") page: String): Call<ThreadListForPagesJsonSchema>

    @GET("/{id}/catalog.json")
    fun getThreadsObservable(@Path("id") boardId: String): Observable<ThreadListJsonSchema>

    @GET("/{id}/{page}.json")
    fun getThreadsForPagesObservable(@Path("id") boardId: String, @Path("page") page: String): Observable<ThreadListForPagesJsonSchema>
}