package com.koresuniku.wishmaster.http.boards_api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.boards_api.model.*

class BoardsJsonSchema : IBaseJsonSchema {

    @SerializedName("Взрослым")
    @Expose
    var adults: List<Adults>? = null
    @SerializedName("Игры")
    @Expose
    var games: List<Games>? = null
    @SerializedName("Политика")
    @Expose
    var politics: List<Politics>? = null
    @SerializedName("Пользовательские")
    @Expose
    var users: List<Users>? = null
    @SerializedName("Разное")
    @Expose
    var other: List<Other>? = null
    @SerializedName("Творчество")
    @Expose
    var creativity: List<Creativity>? = null
    @SerializedName("Тематика")
    @Expose
    var subject: List<Subjects>? = null
    @SerializedName("Техника и софт")
    @Expose
    var tech: List<Tech>? = null
    @SerializedName("Японская культура")
    @Expose
    var japanese: List<Japanese>? = null

}