package com.koresuniku.wishmaster.ui.single_thread

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.koresuniku.wishmaster.ui.controller.view_interface.INotifyableListViewAdapter

class SingleThreadListViewAdapter(val mView: SingleThreadListViewView) :
        BaseAdapter(), INotifyableListViewAdapter {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return View(parent!!.context)
    }

    override fun getItem(position: Int): Any {
        return Any()
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return 0
    }

    override fun iNotifyDataSetChanged() {
        this.notifyDataSetChanged()
    }
}