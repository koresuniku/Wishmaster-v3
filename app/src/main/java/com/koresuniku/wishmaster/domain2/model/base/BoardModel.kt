package com.koresuniku.wishmaster.domain2.model.base

class BoardModel {
    lateinit private var mBoardName: String
    lateinit private var mDefaultName: String

    fun setBoardName(boardName: String) { this.mBoardName = boardName }

    fun getBoardName() = mBoardName

    fun setDefaultName(defaultName: String) { this.mDefaultName = defaultName }

    fun getDefaultName() = mDefaultName
}