package com.koresuniku.wishmaster.http.boards_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Subjects {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

    var isPreferred: Boolean? = null
}
