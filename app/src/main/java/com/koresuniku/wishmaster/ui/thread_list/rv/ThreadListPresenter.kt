package com.koresuniku.wishmaster.ui.thread_list.rv

import android.os.AsyncTask
import com.koresuniku.wishmaster.base.BaseRecyclerViewPresenter
import com.koresuniku.wishmaster.domain.Dvach
import com.koresuniku.wishmaster.domain.thread_list_api.model.Thread
import com.koresuniku.wishmaster.domain2.DataLoader
import com.koresuniku.wishmaster.domain2.model.thread_list.ThreadListData
import com.koresuniku.wishmaster.ui.text.SpanTagHandlerCompat
import com.koresuniku.wishmaster.ui.text.TextUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ThreadListPresenter(private val mRecyclerViewAdapterView: RecyclerViewAdapterView,
                          private val mBoardId: String) :
        BaseRecyclerViewPresenter<ThreadListData>(), DataLoader.OnDataLoadedCallback<ThreadListData> {
    val LOG_TAG: String = ThreadListPresenter::class.java.simpleName

    override lateinit var mData: ThreadListData

    override fun setData(data: ThreadListData) { this.mData = data }

    override fun getData(): ThreadListData = mData

    fun loadData(boardId: String) {
        mRecyclerViewAdapterView.showProgressBar()
        DataLoader.loadThreadListData(boardId, this)
    }

    override fun onDataLoadedSuccessFully(data: ThreadListData) {
        mRecyclerViewAdapterView.hideProgressBar()
        setData(data)
        mRecyclerViewAdapterView.attachAdapter()
    }

    override fun onDataLoadingFailed() {
        mRecyclerViewAdapterView.hideProgressBar()
    }

    fun bindViewHolder(holder: ThreadListRecyclerViewViewHolder, position: Int) {
        val thread = getData().getItemList()[position]

        holder.provideFiles(thread.getFiles())
        holder.setImages()

        setupSubject(holder, thread)
        setupMaxLines(holder)
        setupComment(holder, thread.getComment(), thread.getNum())
        setupPostsAndFilesTextView(holder, thread.getPostsCount(), thread.getFilesCount())
    }

    private fun setupSubject(holder: ThreadListRecyclerViewViewHolder, thread: Thread) {
        if (Dvach.disableSubject.contains(mBoardId) || thread.getSubject().isEmpty()) {
            holder.setNoSubject()
        } else {
            holder.setHasSubject(thread.getSubject())
        }
    }

    private fun setupMaxLines(holder: ThreadListRecyclerViewViewHolder) {
        holder.setMaxLines()
    }

    private fun setupComment(holder: ThreadListRecyclerViewViewHolder, comment: String, threadNum: String) {
        object : AsyncTask<Void, Void, Short>() {
            lateinit var commentDocument: Document
            lateinit var commentElements: Elements

            override fun doInBackground(vararg params: Void?): Short {
                commentDocument = Jsoup.parse(comment)
                commentElements = commentDocument.select(SpanTagHandlerCompat.SPAN_TAG)

                commentElements.forEach { it.getElementsByAttributeValue(
                        SpanTagHandlerCompat.CLASS_ATTR, SpanTagHandlerCompat.QUOTE_VALUE)
                        .tagName(SpanTagHandlerCompat.QUOTE_TAG)
                }
                commentElements.forEach { it.getElementsByAttributeValue(
                        SpanTagHandlerCompat.CLASS_ATTR, SpanTagHandlerCompat.SPOILER_VALUE)
                        .tagName(SpanTagHandlerCompat.SPOILER_TAG)
                }

                return 0
            }

            override fun onPostExecute(result: Short) {
                holder.setComment(commentDocument.html(), threadNum)
            }
        }.execute()
    }

    private fun setupPostsAndFilesTextView(holder: ThreadListRecyclerViewViewHolder,
                                           postsCount: String, filesCount: String) {
        object : AsyncTask<Void, Void, Short>() {
            lateinit var postsAndFilesInfo: String

            override fun doInBackground(vararg params: Void?): Short {
                postsAndFilesInfo = TextUtils.getPostsAndFilesString(
                        postsCount.toInt(), filesCount.toInt())
                return 0
            }

            override fun onPostExecute(result: Short?) {
                holder.setPostsAndFilesTextView(postsAndFilesInfo)
            }
        }.execute()
    }
}