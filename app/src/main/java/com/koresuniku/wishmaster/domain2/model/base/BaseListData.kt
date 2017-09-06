package com.koresuniku.wishmaster.domain2.model.base

open class BaseListData<L> {
    lateinit private var mBoardModel: BoardModel
    lateinit private var mItemList: List<L>

    fun setBoardModel(boardModel: BoardModel) { this.mBoardModel = boardModel }

    fun getBoardModel() = mBoardModel

    fun setItemList(itemList: List<L>) { this.mItemList = itemList }

    fun getItemList() = mItemList
}