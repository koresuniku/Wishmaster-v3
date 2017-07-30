package com.koresuniku.wishmaster.ui.single_thread

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.View.MeasureSpec
import android.opengl.ETC1.getWidth
import android.widget.ListView
import android.widget.TextView
import com.koresuniku.wishmaster.R
import org.jetbrains.anko.find

class AnswersListViewAdapter(var mViewsList: List<View>) : BaseAdapter() {
    val LOG_TAG: String = AnswersListViewAdapter::class.java.simpleName

    fun setViewsList(views: List<View>) {
        mViewsList = views
        notifyDataSetChanged()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        //Log.d(LOG_TAG, "getView: $p0")
        var convertView = p1
        convertView = mViewsList[p0]
        convertView.find<TextView>(R.id.answers).bringToFront()
        return convertView
    }

    override fun getItem(p0: Int): Any {
        return mViewsList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return mViewsList.size
    }

    companion object {
        fun setListViewHeightBasedOnChildren(listView: ListView) {
            val listAdapter = listView.adapter ?: return

            val desiredWidth = MeasureSpec.makeMeasureSpec(listView.width, MeasureSpec.UNSPECIFIED)
            var totalHeight = 0
            var view: View? = null
            for (i in 0..listAdapter.count - 1) {
                view = listAdapter.getView(i, view, listView)
                if (i == 0)
                    view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

                view.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
                totalHeight += view.measuredHeight
            }
            val params = listView.layoutParams
            params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
            listView.layoutParams = params
        }
    }


}