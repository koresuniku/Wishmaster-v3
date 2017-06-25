package com.koresuniku.wishmaster.http.thread_list_api

import android.os.AsyncTask
import android.util.Log
import com.koresuniku.wishmaster.http.HttpClient
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.Thread
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadForPage
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListForPagesJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.ui.view.LoadDataView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList

class ThreadListForPagesAsyncTask(val mView: LoadDataView) : AsyncTask<Void, Void, Void>() {
    private val LOG_TAG = ThreadListForPagesAsyncTask::class.java.simpleName

    private var pagesCount: Int = 0
    private val threadList: MutableList<Thread>
    private var mLocalSchema: ThreadListJsonSchema? = null

    init {
        threadList = ArrayList<com.koresuniku.wishmaster.http.thread_list_api.model.Thread>()
    }

    private fun getThreadsForThreadList(response: Response<ThreadListForPagesJsonSchema>) {
        var thread: com.koresuniku.wishmaster.http.thread_list_api.model.Thread
        var threadForPage: ThreadForPage
        for (i in 0..response.body().getThreads().size - 1) {
            thread = response.body().getThreads()[i].getPosts()[0]
            threadForPage = response.body().getThreads()[i]

            thread.setNum(threadForPage.getThreadNum())
            thread.setFilesCount(threadForPage.getFilesCount())
            thread.setPostsCount(threadForPage.getPostsCount())

            threadList.add(thread)
        }
    }

    override fun doInBackground(vararg params: Void): Void? {
        val callMainSchema = HttpClient.threadsService.getThreads(mView.getBoardId()!!)
        try {
            this.mLocalSchema = callMainSchema.execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val call = HttpClient.threadsService.getThreadsForPages(mView.getBoardId()!!, "index")

        call.enqueue(object : Callback<ThreadListForPagesJsonSchema> {
            override fun onResponse(call: Call<ThreadListForPagesJsonSchema>, response: Response<ThreadListForPagesJsonSchema>) {
                pagesCount = response.body().getPages().size
                Log.d(LOG_TAG, "pagesCount: " + pagesCount)

                getThreadsForThreadList(response)

                for (i in 1..pagesCount - 1 - 1) {
                    val iCopy = i
                    Log.d(LOG_TAG, "loading page " + i)
                    val callForPage = HttpClient.threadsService.getThreadsForPages(mView.getBoardId()!!, i.toString())
                    callForPage.enqueue(object : Callback<ThreadListForPagesJsonSchema> {
                        override fun onResponse(call: Call<ThreadListForPagesJsonSchema>,
                                                response: Response<ThreadListForPagesJsonSchema>) {
                            object : AsyncTask<Void, Void, Void>() {
                                override fun doInBackground(vararg params: Void): Void? {
                                    getThreadsForThreadList(response)
                                    if (iCopy == pagesCount - 2) {
                                        finish()
                                    }
                                    return null
                                }

                                override fun onPostExecute(aVoid: Void) {

                                }
                            }.execute()
                        }

                        override fun onFailure(call: Call<ThreadListForPagesJsonSchema>, t: Throwable) {

                        }
                    })
                }
            }

            override fun onFailure(call: Call<ThreadListForPagesJsonSchema>, t: Throwable) {
                Log.d(LOG_TAG, "failure with d")
            }
        })
        return null
    }

    private fun finish() {
        this.mLocalSchema!!.setThreads(threadList)
        mView.getActivity().runOnUiThread({
            Log.d(LOG_TAG, "finish: ")
            mView.onDataLoaded(listOf(this.mLocalSchema as IBaseJsonSchema))
        })
    }

    override fun onPostExecute(aVoid: Void) {

    }
}