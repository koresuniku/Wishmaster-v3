package com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view

interface ItemTouchHelperAdapter {
    fun onItemMoved(fromPosition: Int, toPosition: Int)

    fun onItemRemoved(position: Int)

    fun onSelectedChanged(actionState: Int)
}