package com.koresuniku.wishmaster.ui.view

interface ItemTouchHelperAdapter {
    fun onItemMoved(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)

    fun onSelectedChanged(actionState: Int)
}