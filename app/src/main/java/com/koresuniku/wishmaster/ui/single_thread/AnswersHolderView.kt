package com.koresuniku.wishmaster.ui.single_thread

import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl

interface AnswersHolderView {
    fun getSchema(): IBaseJsonSchemaImpl

    fun notifyNewAnswersTextViews()
}