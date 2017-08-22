package com.koresuniku.wishmaster.ui.thread_list.rv

import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema

interface RvAdapterView {
    fun setSchema(schema: IBaseJsonSchema)

    fun showProgressBar()

    fun hideProgressBar()

}