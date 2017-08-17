package com.koresuniku.wishmaster.http

/**
 * Created by koresuniku on 17.08.17.
 */
object BaseJsonSchemaMapper {
    val LOG_TAG: String = BaseJsonSchemaMapper::class.java.simpleName

    fun setPostNumbersAscending(schema: IBaseJsonSchema) {
        var counter = 0
        schema.getPosts()!!.forEach { it.setPostNumberAsc(++counter) }
    }
}