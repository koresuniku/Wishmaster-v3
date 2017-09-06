package com.koresuniku.wishmaster.domain.thread_list_api

import android.util.Log
import com.koresuniku.wishmaster.domain.HttpClient
import com.koresuniku.wishmaster.domain.IBaseJsonSchema
import com.koresuniku.wishmaster.domain.thread_list_api.model.Thread
import com.koresuniku.wishmaster.domain.thread_list_api.model.ThreadForPage
import com.koresuniku.wishmaster.domain.thread_list_api.model.ThreadListForPagesJsonSchema
import com.koresuniku.wishmaster.domain.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.ui.controller.view_interface.LoadDataView
import org.jetbrains.anko.doAsync
import retrofit2.Response
import java.util.ArrayList

class ThreadListForPagesAsync(val mView: LoadDataView) {
    private val LOG_TAG = ThreadListForPagesAsync::class.java.simpleName

    private val threadList: ArrayList<Thread> = ArrayList()
    private var mLocalSchema: ThreadListJsonSchema = ThreadListJsonSchema()

    private fun getThreadsForThreadList(response: Response<ThreadListForPagesJsonSchema>) {
        var thread: com.koresuniku.wishmaster.domain.thread_list_api.model.Thread
        var threadForPage: ThreadForPage
        for (i in 0 until response.body().getThreads().size) {
            thread = response.body().getThreads()[i].getPosts()[0]
            threadForPage = response.body().getThreads()[i]

            thread.setNum(threadForPage.getThreadNum())
            thread.setFilesCount(threadForPage.getFilesCount())
            thread.setPostsCount(threadForPage.getPostsCount())

            threadList.add(thread)
        }
    }

    fun getThreadList() {
        doAsync {
            val callMainSchema = HttpClient.threadsService.getThreads(mView.getBoardId()!!)
            mLocalSchema = callMainSchema.execute().body()

            val call = HttpClient.threadsService.getThreadsForPages(mView.getBoardId()!!, "index")
            var response = call.execute()
            val pagesCount = response.body().getPages().size
            getThreadsForThreadList(response)

            for (i in 1..pagesCount - 2) {
                val callForPage = HttpClient.threadsService.getThreadsForPages(mView.getBoardId()!!, i.toString())
                response = callForPage.execute()
                getThreadsForThreadList(response)
                if (i == pagesCount - 2) break
            }

            finish()
        }
    }

    private fun finish() {
        this.mLocalSchema.setThreads(threadList)
        mView.getActivity().runOnUiThread({
            Log.d(LOG_TAG, "finish: ")
            mView.onDataLoaded(listOf(this.mLocalSchema as IBaseJsonSchema))
        })
    }
}