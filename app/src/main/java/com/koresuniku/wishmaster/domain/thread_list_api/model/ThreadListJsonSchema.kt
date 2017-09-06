package com.koresuniku.wishmaster.domain.thread_list_api.model

import com.koresuniku.wishmaster.domain.single_thread_api.model.Post

class ThreadListJsonSchema : com.koresuniku.wishmaster.domain.IBaseJsonSchema {
    @com.google.gson.annotations.SerializedName("BoardName")
    @com.google.gson.annotations.Expose
    private var boardName: String? = null
    @com.google.gson.annotations.SerializedName("default_name")
    @com.google.gson.annotations.Expose
    private var defaultName: String? = null
    @com.google.gson.annotations.SerializedName("threads")
    @com.google.gson.annotations.Expose
    private var threads: List<com.koresuniku.wishmaster.domain.thread_list_api.model.Thread>? = null

    protected fun ThreadsJsonSchema(`in`: android.os.Parcel) {
        defaultName = `in`.readString()
    }

    fun getBoardName(): String {
        return boardName!!
    }

    fun setBoardName(boardName: String) {
        this.boardName = boardName
    }

    fun getDefaultName(): String {
        return defaultName!!
    }

    fun setDefaultName(defaultName: String) {
        this.defaultName = defaultName
    }

    fun getThreads(): List<com.koresuniku.wishmaster.domain.thread_list_api.model.Thread> {
        return threads!!
    }

    fun setThreads(threads: List<com.koresuniku.wishmaster.domain.thread_list_api.model.Thread>) {
        this.threads = threads
    }

    override fun setPosts(posts: List<Post>?) {

    }

    override fun getPosts(): List<Post>? {
        return null
    }
}