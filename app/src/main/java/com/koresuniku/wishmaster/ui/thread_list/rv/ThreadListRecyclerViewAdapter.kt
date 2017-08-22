package com.koresuniku.wishmaster.ui.thread_list.rv

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.widget.FixedRecyclerView


class ThreadListRecyclerViewAdapter(private val mActivity: Activity, val boardId: String) :
        RecyclerView.Adapter<ThreadListRecyclerViewViewHolder>(), RvAdapterView {
    val LOG_TAG: String = ThreadListRecyclerViewAdapter::class.java.simpleName

    private val mRvPresenter = RvPresenter<ThreadListJsonSchema>(this)

    var mRvLayout = LayoutInflater.
            from(mActivity)
            .inflate(R.layout.activity_thread_list_rv_content, null, false)
    var mRecyclerView: FixedRecyclerView = mRvLayout.findViewById(R.id.rv) as FixedRecyclerView

    var mSchema: ThreadListJsonSchema = ThreadListJsonSchema()

    fun initAdapter() {
        mRvPresenter.loadData(boardId)
    }

    override fun showProgressBar() {
        Log.d(LOG_TAG, "showProgressBar")
    }

    override fun hideProgressBar() {
        Log.d(LOG_TAG, "hideProgressBar")
        mActivity.runOnUiThread {
            
            mRecyclerView.adapter = this
        }
    }

    override fun setSchema(schema: IBaseJsonSchema) {
        mSchema = schema as ThreadListJsonSchema
        Log.d(LOG_TAG, "mSchema.count: ${mSchema.getThreads().size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThreadListRecyclerViewViewHolder {
        var itemView: View? = null
        when (viewType) {
            ListViewAdapterUtils.ITEM_NO_IMAGES -> {
                itemView = LayoutInflater.from(mActivity)
                        .inflate(R.layout.thread_item_no_images, parent, false)
            }
            ListViewAdapterUtils.ITEM_SINGLE_IMAGE -> {
                itemView = LayoutInflater.from(mActivity)
                        .inflate(R.layout.thread_item_single_image, parent, false)
            }
            ListViewAdapterUtils.ITEM_MULTIPLE_IMAGES -> {
                itemView = LayoutInflater.from(mActivity)
                        .inflate(R.layout.thread_item_multiple_images, parent, false)
            }
        }
        if (itemView == null) itemView = View(mActivity)
        return ThreadListRecyclerViewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ThreadListRecyclerViewViewHolder?, position: Int) {
        Log.d(LOG_TAG, "onBinfViewHolder: $position")
    }

    override fun getItemCount(): Int = mSchema.getThreads().size

    override fun getItemViewType(position: Int): Int {
        val size = mSchema.getThreads()[position].getFiles().size
        if (size == 0) return ListViewAdapterUtils.ITEM_NO_IMAGES
        if (size == 1) return ListViewAdapterUtils.ITEM_SINGLE_IMAGE
        if (size > 1) return ListViewAdapterUtils.ITEM_MULTIPLE_IMAGES
        return ListViewAdapterUtils.ITEM_NO_IMAGES
    }


}