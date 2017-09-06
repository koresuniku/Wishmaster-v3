package com.koresuniku.wishmaster.domain2

import com.koresuniku.wishmaster.domain.Dvach
import com.koresuniku.wishmaster.domain2.model.thread_list.ThreadListData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object DataLoader {

    interface OnDataLoadedCallback<in S> {
        fun onDataLoadedSuccessFully(data: S)
        fun onDataLoadingFailed()
    }

    fun loadThreadListData(boardId: String, callback: OnDataLoadedCallback<ThreadListData>) {
        if (Dvach.threadForPagesBoards.contains(boardId)) {
            //ThreadListForPagesRvAsyncTask(this, boardId)
        } else {
            HttpClient.threadsService.getThreadsObservable(boardId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { callback.onDataLoadingFailed() }
                    .map(ResponseMapper::mapThreadListData)
                    .subscribe(callback::onDataLoadedSuccessFully)
        }
    }
}