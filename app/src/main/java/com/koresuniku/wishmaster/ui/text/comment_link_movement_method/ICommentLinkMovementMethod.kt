package com.koresuniku.wishmaster.ui.text.comment_link_movement_method

import android.content.Context
import android.text.Spannable
import android.text.style.URLSpan

interface ICommentLinkMovementMethod {
    fun setSpoilerSpan(buffer: Spannable, start: Int, end: Int)

    fun setQuoteSpan(buffer: Spannable, start: Int, end: Int)

    fun setLinkHighlightedSpan(buffer: Spannable, start: Int, end: Int)

    fun setLinkSpan(buffer: Spannable, start: Int, end: Int)

    fun onClickNoSpoilersOrLinksFound()

    fun onLongClickNoSpoilersOrLinksFound()

    fun on2chAnswerLinkClicked(number: String)

    fun highlightLink(buffer: Spannable, span: URLSpan)

    fun unhighlightLink(buffer: Spannable)

    fun getContext(): Context
}