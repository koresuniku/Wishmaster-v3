package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Activity
import android.content.Context
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.ui.controller.view_interface.IDialogAdapter

interface AnswersManagerView {
    fun getSchema(): IBaseJsonSchemaImpl

    fun notifyNewAnswersTextViews()

    fun openAnswersDialog()

    fun getDialogAdapter(): IDialogAdapter

    fun getContext(): Context

    fun getActivity(): Activity

}