package com.koresuniku.wishmaster.http.thread_list_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Thread {
    @SerializedName("comment")
    @Expose
    private var comment: String? = null
    @SerializedName("date")
    @Expose
    private var date: String? = null
    @SerializedName("files")
    @Expose
    private var files: List<Files>? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("num")
    @Expose
    private var num: String? = null
    @SerializedName("files_count")
    @Expose
    private var filesCount: String? = null
    @SerializedName("posts_count")
    @Expose
    private var postsCount: String? = null
    @SerializedName("trip")
    @Expose
    private var trip: String? = null
    @SerializedName("subject")
    @Expose
    private var subject: String? = null

    fun getSubject(): String {
        return subject!!
    }

    fun setSubject(subject: String) {
        this.subject = subject
    }

    fun getComment(): String {
        return comment!!
    }

    fun setComment(comment: String) {
        this.comment = comment
    }

    fun getDate(): String {
        return date!!
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun getFiles(): List<Files> {
        return files!!
    }

    fun setFiles(files: List<Files>) {
        this.files = files
    }

    fun getName(): String {
        return name!!
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getNum(): String {
        return num!!
    }

    fun setNum(num: String) {
        this.num = num
    }

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

    fun getTrip(): String {
        return trip!!
    }

    fun setTrip(trip: String) {
        this.trip = trip
    }
}