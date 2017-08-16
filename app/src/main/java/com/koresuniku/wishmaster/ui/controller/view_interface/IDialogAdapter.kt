package com.koresuniku.wishmaster.ui.controller.view_interface

import android.view.View
import android.view.ViewGroup

interface IDialogAdapter {
    fun getViewForDialog(position: Int, convertView: View?, parent: ViewGroup): View
}