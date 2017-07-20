package com.koresuniku.wishmaster.http.single_thread_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.http.thread_list_api.model.Files

class Post {
    @SerializedName("comment")
    @Expose
    private var comment: String? = null
    @SerializedName("date")
    @Expose
    private var date: String? = null
    @SerializedName("email")
    @Expose
    private var email: String? = null
    @SerializedName("num")
    @Expose
    private var num: String? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("trip")
    @Expose
    private var trip: String? = null
    @SerializedName("subject")
    @Expose
    private var subject: String? = null
    @SerializedName("op")
    @Expose
    private var op: String? = null
    @SerializedName("files")
    @Expose
    private var files: List<Files>? = null

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

    fun getEmail(): String {
        return email!!
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getNum(): String {
        return num!!
    }

    fun setNum(num: String) {
        this.num = num
    }

    fun getName(): String {
        return name!!
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getTrip(): String {
        return trip!!
    }

    fun setTrip(trip: String) {
        this.trip = trip
    }

    fun getSubject(): String {
        return subject!!
    }

    fun setSubject(subject: String) {
        this.subject = subject
    }

    fun getOp(): String {
        return op!!
    }

    fun setOp(op: String) {
        this.op = op
    }

    fun getFiles(): List<Files> {
        return files!!
    }

    fun setFiles(files: List<Files>) {
        this.files = files
    }
}