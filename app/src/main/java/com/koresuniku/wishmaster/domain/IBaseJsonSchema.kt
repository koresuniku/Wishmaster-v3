package com.koresuniku.wishmaster.domain

import com.koresuniku.wishmaster.domain.single_thread_api.model.Post

interface IBaseJsonSchema {
    fun setPosts(posts: List<Post>?)

    fun getPosts(): List<Post>?
}
