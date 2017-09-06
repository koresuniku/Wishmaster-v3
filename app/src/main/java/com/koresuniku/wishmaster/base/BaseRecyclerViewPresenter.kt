package com.koresuniku.wishmaster.base


abstract class BaseRecyclerViewPresenter<S> {
    abstract var mData: S

    abstract fun setData(data: S)

    abstract fun getData(): S
}
