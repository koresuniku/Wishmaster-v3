package com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view

import android.support.v7.widget.RecyclerView

interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}