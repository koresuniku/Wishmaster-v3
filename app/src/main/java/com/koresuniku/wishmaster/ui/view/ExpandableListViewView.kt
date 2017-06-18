package com.koresuniku.wishmaster.ui.view

import android.app.Activity
import android.widget.ExpandableListView
import com.koresuniku.wishmaster.http.boards_api.model.BoardsJsonSchema

interface ExpandableListViewView {
    fun getActivity(): Activity

    fun getSchema(): BoardsJsonSchema

    fun onBoardsSelected(boardId: String, boardName: String)

}