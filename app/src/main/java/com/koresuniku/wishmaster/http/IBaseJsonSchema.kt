package com.koresuniku.wishmaster.http

import com.koresuniku.wishmaster.http.single_thread_api.model.Post

interface IBaseJsonSchema {
    fun setPosts(posts: List<Post>?)

    fun getPosts(): List<Post>?
}
