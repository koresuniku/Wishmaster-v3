package com.koresuniku.wishmaster.ui.controller


interface ClickableAdapter {
    fun onClick(threadNumber: String)

    fun onLongClick(threadNumber: String)
}