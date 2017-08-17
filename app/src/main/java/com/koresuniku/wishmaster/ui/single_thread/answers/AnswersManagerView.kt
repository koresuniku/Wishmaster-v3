package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Activity
import android.content.Context
import com.koresuniku.wishmaster.http.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.ui.controller.view_interface.IDialogAdapter

interface AnswersManagerView {
    fun getSchema(): BaseJsonSchemaImpl

    fun notifyNewAnswersTextViews()

    fun openAnswersDialog()

    fun getDialogAdapter(): IDialogAdapter

    fun getContext(): Context

    fun getActivity(): Activity

}