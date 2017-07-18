package com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view

class SimpleItemTouchItemCallback(private val adapter: ItemTouchHelperAdapter) : android.support.v7.widget.helper.ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: android.support.v7.widget.RecyclerView, viewHolder: android.support.v7.widget.RecyclerView.ViewHolder): Int {
        val dragFlags = android.support.v7.widget.helper.ItemTouchHelper.UP or android.support.v7.widget.helper.ItemTouchHelper.DOWN
        val swipeFlags = android.support.v7.widget.helper.ItemTouchHelper.START or android.support.v7.widget.helper.ItemTouchHelper.END
        return android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: android.support.v7.widget.RecyclerView, viewHolder: android.support.v7.widget.RecyclerView.ViewHolder, target: android.support.v7.widget.RecyclerView.ViewHolder): Boolean {
        adapter.onItemMoved(viewHolder.adapterPosition,
                target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: android.support.v7.widget.RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemRemoved(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: android.support.v7.widget.RecyclerView.ViewHolder?, actionState: Int) {
        adapter.onSelectedChanged(actionState)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }


}