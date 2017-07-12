package com.koresuniku.wishmaster.ui.view

import android.app.Activity
import com.koresuniku.wishmaster.http.IBaseJsonSchema

interface LoadDataView {
    fun loadData()

    fun onDataLoaded(schema: List<IBaseJsonSchema>)

    fun getActivity(): Activity

    fun showProgressBar()

    fun getBoardId(): String?
}
