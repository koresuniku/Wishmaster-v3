package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity
import com.koresuniku.wishmaster.domain.boards_api.BoardsJsonSchema

interface ExpandableListViewView {
    fun getActivity(): Activity

    fun getSchema(): BoardsJsonSchema

    fun openBoard(boardId: String, boardName: String)

    fun getFavouritesFragment(): FavouritesFragment

    fun showChoiceDialog(removeFromFavourites: Boolean, boardId: String, boardName: String)

}