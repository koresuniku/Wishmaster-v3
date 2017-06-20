package com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view

interface ItemTouchHelperAdapter {
    fun onItemMoved(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)

    fun onSelectedChanged(actionState: Int)
}