package com.koresuniku.wishmaster.ui.thread_list.rv

import android.util.Log
import com.koresuniku.wishmaster.http.HttpClient
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.ThreadListForPagesAsync
import com.koresuniku.wishmaster.http.thread_list_api.model.Thread
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadForPage
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListForPagesJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import org.jetbrains.anko.doAsync
import retrofit2.Response
import java.util.ArrayList


class ThreadListForPagesRvAsyncTask(val callback: OnCompletionCallback, boardId: String) {
    private val LOG_TAG = ThreadListForPagesAsync::class.java.simpleName

    private val threadList: ArrayList<Thread> = ArrayList()
    private var mLocalSchema: ThreadListJsonSchema = ThreadListJsonSchema()

    interface OnCompletionCallback {
        fun onSchemaReceived(schema: List<IBaseJsonSchema>)
    }

    init {
        getThreadList(boardId)
    }

    private fun getThreadsForThreadList(response: Response<ThreadListForPagesJsonSchema>) {
        var thread: com.koresuniku.wishmaster.http.thread_list_api.model.Thread
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

    fun getThreadList(boardId: String) {
        doAsync {
            val callMainSchema = HttpClient.threadsService.getThreads(boardId)
            mLocalSchema = callMainSchema.execute().body()

            val call = HttpClient.threadsService.getThreadsForPages(boardId, "index")
            var response = call.execute()
            val pagesCount = response.body().getPages().size
            getThreadsForThreadList(response)

            for (i in 1..pagesCount - 2) {
                val callForPage = HttpClient.threadsService.getThreadsForPages(boardId, i.toString())
                response = callForPage.execute()
                getThreadsForThreadList(response)
                if (i == pagesCount - 2) break
            }

            finish()
        }
    }

    private fun finish() {
        this.mLocalSchema.setThreads(threadList)
        callback.onSchemaReceived(listOf(this.mLocalSchema as IBaseJsonSchema))
    }
}