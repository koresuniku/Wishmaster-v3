package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity

interface FavouritesFragmentView {
    fun getActivity(): Activity

    fun updateBoardListFragment()

    fun showChoiceDialog(removeFromFavourites: Boolean, boardId: String)
}