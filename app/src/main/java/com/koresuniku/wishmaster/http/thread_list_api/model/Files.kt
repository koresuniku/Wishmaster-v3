package com.koresuniku.wishmaster.http.thread_list_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Files {
    @SerializedName("height")
    @Expose
    private var height: String? = null
    @SerializedName("width")
    @Expose
    private var width: String? = null
    @SerializedName("path")
    @Expose
    private var path: String? = null
    @SerializedName("thumbnail")
    @Expose
    private var thumbnail: String? = null
    @SerializedName("size")
    @Expose
    private var size: String? = null
    @SerializedName("displayname")
    @Expose
    private var displayName: String? = null

    fun getDisplayName(): String {
        return displayName!!
    }

    fun setDisplayName(displayName: String) {
        this.displayName = displayName
    }

    fun getHeight(): String {
        return height!!
    }

    fun setHeight(height: String) {
        this.height = height
    }

    fun getWidth(): String {
        return width!!
    }

    fun setWidth(width: String) {
        this.width = width
    }

    fun getPath(): String {
        return path!!
    }

    fun setPath(path: String) {
        this.path = path
    }

    fun getThumbnail(): String {
        return thumbnail!!
    }

    fun setThumbnail(thumbnail: String) {
        this.thumbnail = thumbnail
    }

    fun getSize(): String {
        return size!!
    }

    fun setSize(size: String) {
        this.size = size
    }
}