package com.koresuniku.wishmaster.domain

import android.util.Log
import com.koresuniku.wishmaster.domain.boards_api.BoardsJsonSchema
import com.koresuniku.wishmaster.domain.single_thread_api.model.Post
import com.koresuniku.wishmaster.domain.thread_list_api.ThreadListForPagesAsync
import com.koresuniku.wishmaster.domain.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.ui.controller.view_interface.LoadDataView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataLoader(private val view: LoadDataView) {
    private val LOG_TAG = DataLoader::class.java.simpleName



//    fun loadData() {
//        DataLoader2.loadData()
//        val call = HttpClient.boardsService.getBoards("get_boards")
//        call.enqueue(object : Callback<BoardsJsonSchema> {
//            override fun onResponse(call: Call<BoardsJsonSchema>, response: Response<BoardsJsonSchema>) {
//                Log.i(LOG_TAG, "onResponse: " + response.body().adults)
//                view.onDataLoaded(listOf(response.body()))
//            }
//
//            override fun onFailure(call: Call<BoardsJsonSchema>, t: Throwable) {
//                Log.d(LOG_TAG, "onFailure: ")
//                t.printStackTrace()
//            }
//        })
//    }

    companion object {
        fun loadData(loadDataView: LoadDataView) {
            HttpClient.boardsService.getBoardsObservable("get_boards")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BoardsJsonSchema> {
                        override fun onError(p0: Throwable) {}

                        override fun onComplete() {}

                        override fun onSubscribe(disposable: Disposable) {}

                        override fun onNext(schema: BoardsJsonSchema) {
                            loadDataView.onDataLoaded(listOf(schema))
                        }
                    })
        }
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

    fun loadData(boardId: String, threadNumber: String) {
        Log.d(LOG_TAG, "loadSingleThreadData:")
        view.showProgressBar()

        val call = HttpClient.singleThreadService.getPosts("get_thread", boardId, threadNumber, 0)
        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                val schema: IBaseJsonSchema = BaseJsonSchemaImpl()
                schema.setPosts(response.body())
                view.onDataLoaded(listOf(schema))

                Log.d(LOG_TAG, "data loaded:")

            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d(LOG_TAG, "failure data loading")
                t.printStackTrace()
            }
        })
    }


}