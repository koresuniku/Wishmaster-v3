package com.koresuniku.wishmaster.http.thread_list_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.http.thread_list_api.model.Thread

class ThreadForPage {
    @SerializedName("files_count")
    @Expose
    private var filesCount: String? = null
    @SerializedName("posts_count")
    @Expose
    private var postsCount: String? = null
    @SerializedName("thread_num")
    @Expose
    private var threadNum: String? = null
    @SerializedName("posts")
    @Expose
    private var posts: List<Thread>? = null

    fun getFilesCount(): String {
        return filesCount!!
    }

    fun setFilesCount(filesCount: String) {
        this.filesCount = filesCount
    }

    fun getPostsCount(): String {
        return postsCount!!
    }

    fun setPostsCount(postsCount: String) {
        this.postsCount = postsCount
    }

    fun getPosts(): List<Thread> {
        return posts!!
    }

    fun setPosts(posts: List<Thread>) {
        this.posts = posts
    }

    fun getThreadNum(): String {
        return threadNum!!
    }

    fun setThreadNum(threadNum: String) {
        this.threadNum = threadNum
    }
}