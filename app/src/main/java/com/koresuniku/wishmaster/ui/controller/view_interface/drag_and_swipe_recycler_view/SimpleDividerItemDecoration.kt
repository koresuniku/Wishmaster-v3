package com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.koresuniku.wishmaster.R

class SimpleDividerItemDecoration(resources: Resources) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable = resources.getDrawable(R.drawable.recycler_view_line_divider)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft.toInt()
        val right = (parent.width - parent.paddingRight).toInt()


        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            return
        }

        outRect.top = mDivider.intrinsicHeight
    }
}