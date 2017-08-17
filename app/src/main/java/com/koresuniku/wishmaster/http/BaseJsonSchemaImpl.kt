package com.koresuniku.wishmaster.http

import com.koresuniku.wishmaster.http.single_thread_api.model.Post

class BaseJsonSchemaImpl : IBaseJsonSchema {
    var postsVar: List<Post>? = null

    override fun setPosts(posts: List<Post>?) {
        postsVar = posts
    }

    override fun getPosts(): List<Post>? {
        return this.postsVar
    }
}