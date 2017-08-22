package com.koresuniku.wishmaster.ui.thread_list.rv

import android.util.Log
import com.koresuniku.wishmaster.http.Dvach
import com.koresuniku.wishmaster.http.HttpClient
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.ThreadListForPagesAsync
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RvPresenter<in S : IBaseJsonSchema>(private val mRvAdapterView: RvAdapterView) :
        ThreadListForPagesRvAsyncTask.OnCompletionCallback {
    val LOG_TAG: String = RvPresenter::class.java.simpleName

    fun loadData(boardId: String) {
        mRvAdapterView.showProgressBar()
        if (Dvach.threadForPagesBoards.contains(boardId)) {
            ThreadListForPagesRvAsyncTask(this, boardId)
        } else {
//            val call = HttpClient.threadsService.getThreads(boardId)
//            call.enqueue(object : Callback<ThreadListJsonSchema> {
//                override fun onResponse(call: Call<ThreadListJsonSchema>,
//                                        response: Response<ThreadListJsonSchema>) {
//                    view.onDataLoaded(listOf(response.body()))
//                }
//
//                override fun onFailure(call: Call<ThreadListJsonSchema>, t: Throwable) {
//                    Log.d(LOG_TAG, "onFailure: ")
//                    t.printStackTrace()
//                }
//            })
            HttpClient.threadsService.getThreadsObservable(boardId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ThreadListJsonSchema> {
                        override fun onNext(schema: ThreadListJsonSchema) {
                            if (schema != null) mRvAdapterView.setSchema(schema)
                            mRvAdapterView.hideProgressBar()
                        }

                        override fun onError(p0: Throwable) {

                        }

                        override fun onSubscribe(p0: Disposable) {

                        }

                        override fun onComplete() {

                        }
                    })
        }
    }

    override fun onSchemaReceived(schema: List<IBaseJsonSchema>) {
        mRvAdapterView.hideProgressBar()
        mRvAdapterView.setSchema(schema[0] as S)

    }
}