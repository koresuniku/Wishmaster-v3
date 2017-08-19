package com.koresuniku.wishmaster.http

import android.util.Log
import com.koresuniku.wishmaster.http.boards_api.BoardsJsonSchema
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object DataLoader2 {
    val LOG_TAG: String = DataLoader2::class.java.simpleName

    fun loadData() {
        HttpClient.boardsService.getBoardsObservable("get_boards")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BoardsJsonSchema> {
                    override fun onError(p0: Throwable) {
                        Log.d(LOG_TAG, "onError:")
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(disposable: Disposable) {

                    }

                    override fun onNext(schema: BoardsJsonSchema) {
                    }
                })

    }
}