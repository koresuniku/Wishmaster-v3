package com.koresuniku.wishmaster.http.boards_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseBoardSchema {
    var id: String? = null
    var name: String? = null
    var category: String? = null
}