package com.koresuniku.wishmaster.ui.thread_list.rv

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema


class ThreadListRecyclerViewAdapter :
        RecyclerView.Adapter<ThreadListRecyclerViewViewHolder>(),
        RvAdapterView<ThreadListJsonSchema> {
    val LOG_TAG: String = ThreadListRecyclerViewAdapter::class.java.simpleName

    val mRvPresenter = RvPresenter(this)
    lateinit var mSchema: IBaseJsonSchema

    fun initAdapter() {

    }

    override fun setSchema(schema: ThreadListJsonSchema) {
        mSchema = schema
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThreadListRecyclerViewViewHolder {
        return ThreadListRecyclerViewViewHolder(null, 0)
    }

    override fun onBindViewHolder(holder: ThreadListRecyclerViewViewHolder?, position: Int) {

    }

    override fun getItemCount(): Int {
        return mSchema.getPosts()!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}