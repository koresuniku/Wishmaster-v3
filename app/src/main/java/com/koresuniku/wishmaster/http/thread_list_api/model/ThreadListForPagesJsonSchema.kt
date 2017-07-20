package com.koresuniku.wishmaster.http.thread_list_api.model

import android.os.Parcel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadForPage

class ThreadListForPagesJsonSchema : IBaseJsonSchema {
    @SerializedName("pages")
    @Expose
    private var pages: List<Int>? = null
    @SerializedName("default_name")
    @Expose
    private var defaultName: String? = null
    @SerializedName("threads")
    @Expose
    private var threads: List<ThreadForPage>? = null

    protected fun ThreadsForPagesJsonSchema(`in`: Parcel) {
        defaultName = `in`.readString()
    }


    fun getDefaultName(): String {
        return defaultName!!
    }

    fun setDefaultName(defaultName: String) {
        this.defaultName = defaultName
    }

    fun getThreads(): List<ThreadForPage> {
        return threads!!
    }

    fun setThreads(threads: List<ThreadForPage>) {
        this.threads = threads
    }

    fun getPages(): List<Int> {
        return pages!!
    }

    fun setPages(pages: List<Int>) {
        this.pages = pages
    }

    override fun setPosts(posts: List<Post>?) {

    }

    override fun getPosts(): List<Post>? {
        return null
    }
}