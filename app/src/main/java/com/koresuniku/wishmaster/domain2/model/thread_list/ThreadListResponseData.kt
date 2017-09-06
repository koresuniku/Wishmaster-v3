package com.koresuniku.wishmaster.domain2.model.thread_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.domain.thread_list_api.model.Thread

class ThreadListResponseData {

    @SerializedName ("BoardName")
    @Expose
    lateinit private var mBoardName: String

    @SerializedName ("default_name")
    @Expose
    lateinit private var mDefaultName: String

    @SerializedName ("threads")
    @Expose
    lateinit private var mThreads: List<Thread>

    fun getBoardName(): String = mBoardName

    fun setBoardName(boardName: String) { this.mBoardName = boardName }

    fun getDefaultName(): String = mDefaultName

    fun setDefaultName(defaultName: String) { this.mDefaultName = defaultName }

    fun getThreads(): List<com.koresuniku.wishmaster.domain.thread_list_api.model.Thread> = mThreads

    fun setThreads(threads: List<Thread>) { this.mThreads = threads }
}