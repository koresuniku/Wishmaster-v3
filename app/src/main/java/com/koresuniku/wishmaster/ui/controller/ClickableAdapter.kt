package com.koresuniku.wishmaster.ui.controller


interface ClickableAdapter {
    fun onClickNoSpoilersOrLinksFound(threadNumber: String)

    fun onLongClickNoSpoilersOrLinksFound(threadNumber: String)

    fun onLongClickLinkFound(link: String)

}