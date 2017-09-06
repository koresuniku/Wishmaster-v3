package com.koresuniku.wishmaster.domain2

import com.koresuniku.wishmaster.domain2.model.base.BoardModel
import com.koresuniku.wishmaster.domain2.model.thread_list.ThreadListData
import com.koresuniku.wishmaster.domain2.model.thread_list.ThreadListResponseData

object ResponseMapper {

    fun mapThreadListData(responseData: ThreadListResponseData): ThreadListData {
        val threadListData = ThreadListData()

        val boardModel = BoardModel()
        boardModel.setBoardName(responseData.getBoardName())
        boardModel.setDefaultName(responseData.getDefaultName())
        threadListData.setBoardModel(boardModel)

        val threadList = responseData.getThreads()
        threadListData.setItemList(threadList)

        return threadListData
    }
}