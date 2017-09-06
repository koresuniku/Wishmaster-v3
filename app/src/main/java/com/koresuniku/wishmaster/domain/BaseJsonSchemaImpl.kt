package com.koresuniku.wishmaster.domain

import com.koresuniku.wishmaster.domain.single_thread_api.model.Post

class BaseJsonSchemaImpl : IBaseJsonSchema {
    var postsVar: List<Post>? = null

    override fun setPosts(posts: List<Post>?) {
        postsVar = posts
    }

    override fun getPosts(): List<Post>? {
        return this.postsVar
    }
}