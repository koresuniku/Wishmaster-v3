package com.koresuniku.wishmaster.ui.single_thread.answers

class ScrollsHolder(val scroll: Int) {
    var firstVisiblePosition: Int? = null
    var top: Int? = null


    constructor(fvp: Int, top: Int) : this(0){
        this.firstVisiblePosition = fvp
        this.top = top
    }
}