package com.koresuniku.wishmaster.http

import android.util.Log
import com.koresuniku.wishmaster.http.boards_api.BoardsJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.ThreadListForPagesAsync
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.ui.view.LoadDataView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataLoader(private val view: LoadDataView) {
    private val LOG_TAG = DataLoader::class.java.simpleName

    fun loadData() {
        val call = HttpClient.boardsService.getBoards("get_boards")
        call.enqueue(object : Callback<BoardsJsonSchema> {
            override fun onResponse(call: Call<BoardsJsonSchema>, response: Response<BoardsJsonSchema>) {
                Log.i(LOG_TAG, "onResponse: " + response.body().adults)
                view.onDataLoaded(listOf(response.body()))
            }

            override fun onFailure(call: Call<BoardsJsonSchema>, t: Throwable) {
                Log.d(LOG_TAG, "onFailure: ")
                t.printStackTrace()
            }
        })
    }

    fun loadData(boardId: String) {
        view.showProgressBar()
        if (Dvach.threadForPagesBoards.contains(boardId)) {
            ThreadListForPagesAsync(view).getThreadList()
        } else {
            val call = HttpClient.threadsService.getThreads(boardId)
            call.enqueue(object : Callback<ThreadListJsonSchema> {
                override fun onResponse(call: Call<ThreadListJsonSchema>,
                                        response: Response<ThreadListJsonSchema>) {
                    view.onDataLoaded(listOf(response.body()))
                }

                override fun onFailure(call: Call<ThreadListJsonSchema>, t: Throwable) {
                    Log.d(LOG_TAG, "onFailure: ")
                    t.printStackTrace()
                }
            })
        }
    }
//
//    fun loadData(boardId: String, threadNumber: String) {
//        Log.d(LOG_TAG, "loadSingleThreadData:")
//        view.showProgressBar()
//
//        val call = HttpClient.singleThreadService.getPosts("get_thread", boardId, threadNumber, 0)
//        call.enqueue(object : Callback<List<Post>> {
//            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                view.onDataLoaded(response.body())
//                Log.d(LOG_TAG, "data loaded:")
//
//            }
//
//            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                Log.d(LOG_TAG, "failure data loading")
//                t.printStackTrace()
//            }
//        })
//    }


}