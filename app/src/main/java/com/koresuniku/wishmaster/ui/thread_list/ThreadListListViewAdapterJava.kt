package com.koresuniku.wishmaster.ui.thread_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.koresuniku.wishmaster.R

class ThreadListListViewAdapterJava internal constructor(private val mContext: Context) : BaseAdapter() {

    internal inner class ViewHolder

    override fun getCount(): Int {
        return 20
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.thread_item_no_images, parent, false)
            holder = ViewHolder()
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        return convertView
    }
}
