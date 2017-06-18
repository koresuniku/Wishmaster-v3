package com.koresuniku.wishmaster.ui.dashboard

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.view.ExpandableListViewView

class BoardsExpandableListViewAdapter(val mView: ExpandableListViewView) : BaseExpandableListAdapter() {
    val mSchema = mView.getSchema()
    val mActivity = mView.getActivity()

    override fun getGroupCount(): Int {
        return 9
    }

    private fun getBoardsGroup(i: Int): List<*>? {
        when (i) {
            0 -> {
                return mSchema.adults
            }
            1 -> {
                return mSchema.games
            }
            2 -> {
                return mSchema.politics
            }
            3 -> {
                return mSchema.users
            }
            4 -> {
                return mSchema.other
            }
            5 -> {
                return mSchema.creativity
            }
            6 -> {
                return mSchema.subject
            }
            7 -> {
                return mSchema.tech
            }
            8 -> {
                return mSchema.japanese
            }
            else -> return null
        }
    }

    private fun getBoardsGroupName(i: Int): String? {
        when (i) {
            0 -> {
                return mActivity.getString(R.string.adult_boards)
            }
            1 -> {
                return mActivity.getString(R.string.games_boards)
            }
            2 -> {
                return mActivity.getString(R.string.politics_boards)
            }
            3 -> {
                return mActivity.getString(R.string.users_boards)
            }
            4 -> {
                return mActivity.getString(R.string.different_boards)
            }
            5 -> {
                return mActivity.getString(R.string.creativity_boards)
            }
            6 -> {
                return mActivity.getString(R.string.subject_boards)
            }
            7 -> {
                return mActivity.getString(R.string.tech_boards)
            }
            8 -> {
                return mActivity.getString(R.string.japanese_boards)
            }
            else -> return null
        }
    }

    override fun getChildrenCount(i: Int): Int {
        return getBoardsGroup(i)!!.size

    }

    override fun getGroup(i: Int): Any {
        return getBoardsGroup(i)!!
    }

    override fun getChild(i: Int, i1: Int): Any {
        return getBoardsGroup(i)!![i1]!!
    }

    override fun getGroupId(i: Int): Long {
        return i.toLong()
    }

    override fun getChildId(i: Int, i1: Int): Long {
        return i1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(i: Int, b: Boolean, view: View?, viewGroup: ViewGroup?): View {
        var customView =  mView.getActivity().layoutInflater
                .inflate(R.layout.boards_exp_listview_child_item, viewGroup, false)

        val groupName = customView.findViewById(R.id.group_board_name) as? TextView
        groupName!!.text = getBoardsGroupName(i)

        val indicator = customView.findViewById(R.id.group_item_indicator) as? ImageView
        if (b)
            indicator!!.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
        else
            indicator!!.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp)

        return customView
    }

    override fun getChildView(i: Int, i1: Int, b: Boolean, view: View?, viewGroup: ViewGroup?): View {
        var view = view
        view = mActivity.layoutInflater
                .inflate(R.layout.boards_exp_listview_child_item, viewGroup, false)

        val boardNameTextView = view.findViewById(R.id.child_board_name) as TextView
        val boardId: String?
        val boardName: String?
        when (i) {
            0 -> {
                boardId = mSchema.adults[i1].id
                boardName = mSchema.adults.get(i1).name
            }
            1 -> {
                boardId = mSchema.games.get(i1).id
                boardName = mSchema.games.get(i1).name
            }
            2 -> {
                boardId = mSchema.politics.get(i1).id
                boardName = mSchema.politics.get(i1).name
            }
            3 -> {
                boardId = mSchema.users.get(i1).id
                boardName = mSchema.users.get(i1).name
            }
            4 -> {
                boardId = mSchema.other.get(i1).id
                boardName = mSchema.other.get(i1).name
            }
            5 -> {
                boardId = mSchema.creativity.get(i1).id
                boardName = mSchema.creativity.get(i1).name
            }
            6 -> {
                boardId = mSchema.subject.get(i1).id
                boardName = mSchema.subject.get(i1).name
            }
            7 -> {
                boardId = mSchema.tech.get(i1).id
                boardName = mSchema.tech.get(i1).name
            }
            8 -> {
                boardId = mSchema.japanese[i1].id
                boardName = mSchema.japanese[i1].name
            }
            else -> {
                boardId = null
                boardName = null
            }
        }
        boardNameTextView.text = "/$boardId/ - $boardName"

        view.setBackgroundDrawable(mActivity.resources.getDrawable(R.drawable.exp_listview_child_selector))

        view.setOnClickListener(View.OnClickListener {
            mView.onBoardsSelected(boardId!!, boardName!!)


        })

        return view
    }

    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return true
    }
}
