package com.koresuniku.wishmaster.http.boards_api

import com.koresuniku.wishmaster.http.boards_api.model.BoardsJsonSchema
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardsApiService {

    @GET("/makaba/mobile.fcgi")
    fun getBoards(@Query("task") task: String): Call<BoardsJsonSchema>
}
