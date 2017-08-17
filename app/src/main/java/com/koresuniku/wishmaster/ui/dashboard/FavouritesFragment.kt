package com.koresuniku.wishmaster.ui.dashboard

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.database.BoardsUtils
import com.koresuniku.wishmaster.database.DatabaseContract
import com.koresuniku.wishmaster.http.boards_api.model.BaseBoardSchema
import com.koresuniku.wishmaster.application.PreferenceUtils
import com.koresuniku.wishmaster.ui.UiUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view.ItemTouchHelperAdapter
import com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view.OnStartDragListener
import com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view.SimpleDividerItemDecoration
import com.koresuniku.wishmaster.ui.controller.view_interface.drag_and_swipe_recycler_view.SimpleItemTouchItemCallback
import java.util.*
import kotlin.collections.ArrayList

class FavouritesFragment(val mView: FavouritesFragmentView) : Fragment(), OnStartDragListener{
    val LOG_TAG: String = FavouritesFragment::class.java.simpleName!!

    var mRootView: View = mView.getActivity().layoutInflater.inflate(R.layout.favourites_fragment, null, false)

    var mNoBoardsContainer: View? = null
    var mRecyclerView: RecyclerView? = null
    var mRecyclerViewAdapter: RecyclerViewAdapter? = null
    var mItemTouchHelper: ItemTouchHelper? = null

    var favouriteBoardsList: ArrayList<BaseBoardSchema>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mNoBoardsContainer = mRootView.findViewById(R.id.no_boards_container)
        mRecyclerView = mRootView.findViewById(R.id.favourites_recycler_view) as RecyclerView?

        notifyOrInitBoardList()

        return mRootView
    }

    fun notifyOrInitBoardList() {
        if (getFavouriteBoardsList()) setupRecyclerView()
        else {
            mRecyclerView!!.visibility = View.GONE
            mNoBoardsContainer!!.visibility = View.VISIBLE
        }
    }

    fun setupRecyclerView() {
        mNoBoardsContainer!!.visibility = View.GONE
        mRecyclerView!!.visibility = View.VISIBLE

        if (mRecyclerViewAdapter != null) mRecyclerViewAdapter!!.notifyDataSetChanged()
        else {
            mRecyclerView!!.addItemDecoration(SimpleDividerItemDecoration(mView.getActivity().resources))
            mRecyclerView!!.layoutManager = LinearLayoutManager(mView.getActivity())
            mRecyclerViewAdapter = RecyclerViewAdapter(this)
            mRecyclerView!!.adapter = mRecyclerViewAdapter

            val callback = SimpleItemTouchItemCallback(mRecyclerViewAdapter!!)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper!!.attachToRecyclerView(mRecyclerView)
        }
    }

    fun getFavouriteBoardsList(): Boolean {
        val rawQueue = PreferenceUtils.getFavouriteBoardsQueue(mView.getActivity())
        if (rawQueue.isEmpty()) return false

        val rawBoardsList: List<String> = rawQueue.substring(1, rawQueue.length).split(Regex(pattern = "\\s"))
        val boardsList: ArrayList<String> = ArrayList()
        rawBoardsList.filterNot { it.isEmpty() }.mapTo(boardsList) { it.replace("/", "") }

        favouriteBoardsList = ArrayList()
        var cursor: Cursor = mView.getActivity().contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI,
                BoardsUtils.mBoardsProjection, null, null, null, null)
        val boardNameIndex: Int = cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME)
        var boardName: String
        for (boardId: String in boardsList) {
            val boardObject: BaseBoardSchema = BaseBoardSchema()

            cursor = BoardsUtils.queryABoard(mView.getActivity(), boardId)
            cursor.moveToFirst()
            boardName = cursor.getString(boardNameIndex)

            boardObject.id = boardId
            boardObject.name = boardName
            favouriteBoardsList!!.add(boardObject)
        }
        cursor.close()
        return true
    }

    fun rewriteBoardsQueue() {
        var newQueue: String = ""

        for (board: BaseBoardSchema in favouriteBoardsList!!) {
            newQueue += " /"
            newQueue += board.id
            newQueue += "/"
        }

        Log.d(LOG_TAG, "rewritten queue: " + newQueue)
        PreferenceUtils.writeInFavouriteBoardsQueue(mView.getActivity(), newQueue)
    }

    fun onBoardRemoved(boardId: String) {
        if (favouriteBoardsList == null) return
        var position: Int = -1
        for (board in favouriteBoardsList!!) {
            Log.d(LOG_TAG, "board.id: " + board.id + ", boardId: " + boardId)
            if (board.id == boardId) {
                position = favouriteBoardsList!!.indexOf(board)
                break
            }
        }
        mRecyclerViewAdapter!!.onItemRemoved(position)
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

        override fun onItemRemoved(position: Int) {
            Log.d(LOG_TAG, "onItemRemoved:")

            val values: ContentValues = ContentValues()
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_PREFERRED,
                    DatabaseContract.BoardsEntry.BOARD_PREFERRED_FALSE)
            mView.getActivity().contentResolver.update(DatabaseContract.BoardsEntry.CONTENT_URI,
                    values, DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                    arrayOf(favouriteBoardsList!![position].id))
            favouriteBoardsList!!.removeAt(position)

            rewriteBoardsQueue()
            notifyItemRemoved(position)

            if (favouriteBoardsList!!.size == 0) {
                mRecyclerView!!.visibility = View.GONE
                mNoBoardsContainer!!.visibility = View.VISIBLE
            } else Handler().postDelayed({this.notifyDataSetChanged()}, 250)
        }

        override fun onSelectedChanged(actionState: Int) {
            if (actionState == 0) {
                Log.d(LOG_TAG, "onSelectedChanged: " + actionState)
                rewriteBoardsQueue()
                Handler().postDelayed({this.notifyDataSetChanged()}, 250)
            }

            Log.d(LOG_TAG, "favboardsCount: " + favouriteBoardsList!!.size)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            Log.d(LOG_TAG, "onBindViewHolder:")

            holder!!.boardNameTextView.text = getTextForTextView(position)

            UiUtils.setImageViewColorFilter(holder.dragAndDropDots, android.R.color.darker_gray)
            holder.dragAndDropDots.setOnLongClickListener({
                mOnStartDragListener.onStartDrag(holder); false
            })

            Log.d(LOG_TAG, "setting long click listener for board: " + favouriteBoardsList!![position].id!!)
            holder.itemContainer.setOnLongClickListener {
                Log.d(LOG_TAG, "sending board: " + favouriteBoardsList!![position].id!!)
                mView.showChoiceDialog(true,
                        favouriteBoardsList!![position].id!!,
                        favouriteBoardsList!![position].name!!); false
            }

            holder.itemContainer.background =
                    mView.getActivity().resources.getDrawable(R.drawable.exp_listview_child_selector)

            holder.itemContainer.setOnClickListener {
                mView.openBoard(
                        boardId = favouriteBoardsList!![position].id!!,
                        boardName = favouriteBoardsList!![position].name!!)
            }

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
            val dragAndDropDots: ImageView = itemView.findViewById(R.id.drag_and_drop) as ImageView
        }

        fun getTextForTextView(position: Int): String {
            var text: String = "/"
            text += favouriteBoardsList!![position].id
            text += "/ - "
            text += favouriteBoardsList!![position].name

            return text
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper!!.startDrag(viewHolder)
    }

}