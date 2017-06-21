package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.system.PreferencesManager
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.ItemTouchHelperAdapter
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.OnStartDragListener
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.SimpleDividerItemDecoration
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.SimpleItemTouchItemCallback
import java.util.*
import kotlin.collections.ArrayList

class FavouritesFragment(val mView: Activity) : Fragment(), OnStartDragListener {


    val LOG_TAG: String = FavouritesFragment::class.java.simpleName!!

    var mRootView: View = mView.layoutInflater.inflate(R.layout.favourites_fragment, null, false)

    var mNoBoardsContainer: View? = null
    var mRecyclerView: RecyclerView? = null
    var mRecyclerViewAdaper: RecyclerViewAdapter? = null
    var mItemTouchHelper: ItemTouchHelper? = null

    var favouriteBoardsList: ArrayList<String>? = ArrayList()



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mNoBoardsContainer = mRootView.findViewById(R.id.no_boards_container)
        mRecyclerView = mRootView.findViewById(R.id.favourites_recycler_view) as RecyclerView?


        getFavouriteBoardsList()
        initRecyclerView()



        return mRootView
    }

//    fun fillTestData() {
//        dummyData = ArrayList()
//        (0..10).map { "test board ${it + 1}" }.forEach { dummyData!!.add(it) }
//    }

    fun initRecyclerView() {
//        mNoBoardsContainer!!.visibility = View.GONE

        mRecyclerView!!.addItemDecoration(SimpleDividerItemDecoration(mView.resources))
        mRecyclerView!!.layoutManager = LinearLayoutManager(mView) as RecyclerView.LayoutManager?
        mRecyclerViewAdaper = RecyclerViewAdapter(this)
        mRecyclerView!!.adapter = mRecyclerViewAdaper

        val callback = SimpleItemTouchItemCallback(mRecyclerViewAdaper!!)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper!!.attachToRecyclerView(mRecyclerView)
    }

    fun getFavouriteBoardsList() {
        val rawQueue = PreferencesManager.getFavouriteBoardsQueue(mView)
        if (rawQueue.isEmpty()) return

        val rawBoardsList: List<String> = rawQueue.substring(1, rawQueue.length).split(Regex("\\s"))
        val boardsList: ArrayList<String> = ArrayList()

        Log.d(LOG_TAG, "raw boards: " + rawBoardsList)

        rawBoardsList.filterNot { it.isEmpty() }.mapTo(boardsList) { it.replace("/", "") }

        Log.d(LOG_TAG, "boards received: " + boardsList)
        favouriteBoardsList = boardsList

    }

    inner class RecyclerViewAdapter(val mOnStartDragListener: OnStartDragListener) :
            RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(),
            ItemTouchHelperAdapter {

        override fun onItemMoved(fromPosition: Int, toPosition: Int) {
            if (fromPosition < toPosition) {
                for (i in fromPosition..toPosition - 1) {
                    Collections.swap(favouriteBoardsList, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(favouriteBoardsList, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemDismiss(position: Int) {

        }

        override fun onSelectedChanged(actionState: Int) {

        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder!!.boardNameTextView.text = favouriteBoardsList!![position]
            holder!!.itemContainer.setOnLongClickListener(View.OnLongClickListener {
                mOnStartDragListener!!.onStartDrag(holder); false
            })

        }

        override fun getItemCount(): Int {
            return favouriteBoardsList!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.favourite_board_item, parent, false))
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val boardNameTextView: TextView = itemView.findViewById(R.id.board_name) as TextView
            val itemContainer: View = itemView.findViewById(R.id.item_container) as View
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper!!.startDrag(viewHolder)
    }

}