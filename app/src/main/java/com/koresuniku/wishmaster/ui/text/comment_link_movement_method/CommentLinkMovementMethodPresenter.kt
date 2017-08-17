package com.koresuniku.wishmaster.ui.text.comment_link_movement_method

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.style.CharacterStyle
import android.text.style.URLSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.koresuniku.wishmaster.system.Constants
import com.koresuniku.wishmaster.ui.text.LinkHighlightSpan
import com.koresuniku.wishmaster.ui.text.QuoteSpan
import com.koresuniku.wishmaster.ui.text.SpoilerSpan
import java.util.regex.Pattern

class CommentLinkMovementMethodPresenter(val linkMovementMethod: ICommentLinkMovementMethod) :
        ICommentLinkMovementMethodPresenter {

    companion object {
        val SPOILER_SPAN_FLAG: Int = 0
        val QUOTE_SPAN_FLAG: Int = 1
        val LINK_SPAN_FLAG: Int = 2
        val LINK_HIGHLIGHTED_FLAG: Int = 3
    }

    val LOG_TAG: String = CommentLinkMovementMethodPresenter::class.java.simpleName

    var isPressed: Boolean = false
    var isLongPressed: Boolean = false
    var onActionDownTime: Long = 0L
    var onActionUpTime: Long = 0L
    var clickTime: Long = 0L
    var currentLink: String = ""

    override fun initFixSpoilerSpans(spannable: Spannable) {
        val spoilerSpans = spannable.getSpans(0, spannable.length, SpoilerSpan::class.java)
        if (spoilerSpans.isNotEmpty()) {
            spoilerSpans.forEach {
                val spoilerStart = spannable.getSpanStart(it)
                val spoilerEnd = spannable.getSpanEnd(it)

                doLayOnSpan(spannable, spoilerStart, spoilerEnd, LINK_SPAN_FLAG, SPOILER_SPAN_FLAG)
                doLayOnSpan(spannable, spoilerStart, spoilerEnd, QUOTE_SPAN_FLAG, SPOILER_SPAN_FLAG)
            }
        }
    }

    fun findUnderlyingSpans(spannable: Spannable, spanToFind: Int, spoilerStart: Int, spoilerEnd: Int):
            Array<out CharacterStyle> {
        var underlyingSpans: Array<out CharacterStyle> = emptyArray()
        when (spanToFind) {
            SPOILER_SPAN_FLAG -> {
                underlyingSpans = spannable.getSpans(spoilerStart, spoilerEnd, SpoilerSpan::class.java)
            }
            QUOTE_SPAN_FLAG -> {
                underlyingSpans = spannable.getSpans(spoilerStart, spoilerEnd, QuoteSpan::class.java)
            }
            LINK_SPAN_FLAG -> {
                underlyingSpans = spannable.getSpans(spoilerStart, spoilerEnd, URLSpan::class.java)
            }
            LINK_HIGHLIGHTED_FLAG -> {
                underlyingSpans = spannable.getSpans(spoilerStart, spoilerEnd, LinkHighlightSpan::class.java)
            }
        }

        return underlyingSpans
    }

    fun doLayOnSpan(spannable: Spannable, spoilerStart: Int, spoilerEnd: Int, spanToFind: Int, spanToLayOn: Int) {
        val underlyingSpans: Array<out CharacterStyle> =
                findUnderlyingSpans(spannable, spanToFind, spoilerStart, spoilerEnd)
        if (underlyingSpans.isNotEmpty()) {
            underlyingSpans.forEach {
                val spoilerStartIsLonger = spoilerStart <= spannable.getSpanStart(it)
                val spoilerEndIsLonger = spoilerEnd >= spannable.getSpanEnd(it)
                val startToSpan: Int
                val endToSpan: Int
                if (spoilerStartIsLonger) startToSpan = spannable.getSpanStart(it)
                else startToSpan = spoilerStart
                if (spoilerEndIsLonger) endToSpan = spannable.getSpanEnd(it)
                else endToSpan = spoilerEnd

                when (spanToLayOn) {
                    SPOILER_SPAN_FLAG -> {
                        linkMovementMethod.setSpoilerSpan(spannable, startToSpan, endToSpan)
                    }
                    QUOTE_SPAN_FLAG -> {
                        linkMovementMethod.setQuoteSpan(spannable, startToSpan, endToSpan)
                    }
                    LINK_SPAN_FLAG -> {
                        linkMovementMethod.setLinkSpan(spannable, startToSpan, endToSpan)
                    }
                    LINK_HIGHLIGHTED_FLAG -> {
                        linkMovementMethod.setLinkHighlightedSpan(spannable, startToSpan, endToSpan)
                    }
                }
            }
        }
    }

    fun locateOffset(widget: TextView, event: MotionEvent): Int {
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= widget.totalPaddingLeft
        y -= widget.totalPaddingTop
        x += widget.scrollX
        y += widget.scrollY
        val layout = widget.layout
        val line = layout.getLineForVertical(y)
        return layout.getOffsetForHorizontal(line, x.toFloat())
    }

    override fun onActionDown(widget: TextView, buffer: Spannable, event: MotionEvent) {
        isPressed = true
        onActionDownTime = System.currentTimeMillis()

        val off = locateOffset(widget, event)
        val isLinkFound = findLinks(widget, off, buffer,
                allowPerformClick = false)
        val isSpoilerFound = findSpoilers(off, buffer, widget,
                doClickSpoiler = false, linkFound = isLinkFound)
        if (!isLinkFound && !isSpoilerFound) {
            widget.postDelayed(onLongClickNoSpoilersOrLinksFoundRunnable, Constants.LONG_CLICK_TIME)
        }
        if (isLinkFound &&
                if (findSpoiler(off, buffer) == null) false
                else findSpoiler(off, buffer)!!.clicked) {
            widget.postDelayed(onLongClickLinkFoundAndSpoilerIsClickedRunnable, Constants.LONG_CLICK_TIME)
        } else Log.d(LOG_TAG, "link isnt found")
    }

    override fun onActionUp(widget: TextView, buffer: Spannable, event: MotionEvent) {
        isPressed = false
        onActionUpTime = System.currentTimeMillis()
        clickTime = onActionUpTime - onActionDownTime

        val off = locateOffset(widget, event)
        val isLinkFound = findLinks(widget, off, buffer,
                allowPerformClick = true)
        val isSpoilerFound = findSpoilers(off, buffer, widget,
                doClickSpoiler = true, linkFound = isLinkFound)

        if (!isLinkFound && !isSpoilerFound) {
            if (!isLongPressed) linkMovementMethod.onClickNoSpoilersOrLinksFound()
            else isLongPressed = false
        }
        if (!isLinkFound && isSpoilerFound) {
            Log.d(LOG_TAG, "onActionUp found spoiler and no links")
        }

    }

    override fun onActionCancel(widget: TextView, buffer: Spannable, event: MotionEvent) {
        widget.removeCallbacks(onLongClickNoSpoilersOrLinksFoundRunnable)
        widget.removeCallbacks(onLongClickLinkFoundAndSpoilerIsClickedRunnable)
    }

    val onLongClickNoSpoilersOrLinksFoundRunnable: Runnable = Runnable {
        if (isPressed) {
            isLongPressed = true
            linkMovementMethod.onLongClickNoSpoilersOrLinksFound()
        } else isLongPressed = false
    }

    val onLongClickLinkFoundAndSpoilerIsClickedRunnable: Runnable = Runnable {
        if (isPressed) {
            isLongPressed = true
            Log.d(LOG_TAG, "link long pressed: $currentLink")
        } else {
            isLongPressed = false
            Log.d(LOG_TAG, "is pressed was false")
        }
    }

    fun findLinks(widget: TextView, off: Int, buffer: Spannable?, allowPerformClick: Boolean): Boolean {
        val links = buffer!!.getSpans(off, off, URLSpan::class.java)

        if (links.isNotEmpty()) {
            Log.d(LOG_TAG, "found link: ${links[0].url}")
            val spoilerSpans = buffer.getSpans(off, off, SpoilerSpan::class.java)
            if (spoilerSpans.isNotEmpty()) {
                if (allowPerformClick) {
                    var returnBecauseOfSpoiler: Boolean = false
                    spoilerSpans.forEach {
                        if (it.clicked) {
                            //it.enabled = false
                        } else {
                            //it.enabled = true
                            //clickSpoilerAndColorizeUnderlying(widget, buffer, it)
                            returnBecauseOfSpoiler = true
                        }
                    }
                    if (returnBecauseOfSpoiler) return true
                }
            }

            if (find2chAnswerLink(links[0].url, allowPerformClick)) return true
            if (find2chInnerLink(links[0].url)) return true
            if (find2chGeneralLink(links[0].url)) return true
            if (findOuterLink(links[0].url, allowPerformClick)) return true
        }

        return false
    }

    fun find2chAnswerLink(url: String, allowPerformClick: Boolean): Boolean {
        var pattern = Pattern.compile("/[a-z]+/res/[0-9]+[.]html#[0-9]+")
        var matcher = pattern.matcher(url)

        if (matcher.find()) {
            pattern = Pattern.compile("#[0-9]+")
            matcher = pattern.matcher(matcher.group())
            if (matcher.find()) {
                //Log.d(LOG_TAG, "2ch answer link: ${matcher.group()}")
                currentLink = matcher.group()
                if (allowPerformClick && clickTime < Constants.CLICK_TIME) {
                    linkMovementMethod.on2chAnswerLinkClicked(
                            matcher.group().substring(1, matcher.group().length))
                }
            }
            return true
        }

        return false
    }

    fun find2chInnerLink(url: String): Boolean {
        val pattern = Pattern.compile("/[a-z]+/res/[0-9]+[.]html")
        val matcher = pattern.matcher(url)

        if (matcher.find()) {
            //TODO: handle inner 2ch link
            currentLink = matcher.group()
            Log.d(LOG_TAG, "inner 2ch link: ${matcher.group()}")

            return true
        }

        return false
    }

    fun find2chGeneralLink(url: String): Boolean {
        val pattern = Pattern.compile("https?://2ch\\.hk.*")
        val matcher = pattern.matcher(url)

        if (matcher.find()) {
            //TODO: handle general 2ch link
            currentLink = matcher.group()
            Log.d(LOG_TAG, "general 2ch link: ${matcher.group()}")
            return true
        }

        return false
    }

    fun findOuterLink(url: String, allowPerformClick: Boolean): Boolean {
        val pattern = Pattern.compile("https?://.+")
        val matcher = pattern.matcher(url)

        if (matcher.find()) {
            Log.d(LOG_TAG, "outer link: ${matcher.group()}")
            Log.d(LOG_TAG, "clickTime: $clickTime")
            currentLink = matcher.group()
            if (allowPerformClick && clickTime < Constants.CLICK_TIME) {
                linkMovementMethod.getContext()
                        .startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(matcher.group())))
            }
            return true
        }

        return false
    }

    fun findSpoilers(off: Int, buffer: Spannable?, widget: TextView?, doClickSpoiler: Boolean, linkFound: Boolean): Boolean {
        val spoilers = buffer!!.getSpans(off, off, SpoilerSpan::class.java)
        if (spoilers.isNotEmpty()) {
            val spoiler = spoilers[0]

            if (doClickSpoiler) {
                Log.d(LOG_TAG, "")
                if (!linkFound || (linkFound && !spoiler.clicked))
                    clickSpoilerAndColorizeUnderlying(widget!!, buffer, spoiler)
            }
            return true
        }
        return false
    }

    fun findSpoiler(off: Int, buffer: Spannable): SpoilerSpan? {
        val spoilers = buffer.getSpans(off, off, SpoilerSpan::class.java)
        if (spoilers.isNotEmpty()) return spoilers[0]
        else return null
    }

    fun clickSpoilerAndColorizeUnderlying(widget: TextView, buffer: Spannable, spoiler: SpoilerSpan) {
        Log.d(LOG_TAG, "doClickSpoiler")

        spoiler.onClick(widget)

        doColorizeUnderlyingOrSpoilerItself(buffer, spoiler.start, spoiler.end, LINK_SPAN_FLAG, spoiler)
        doColorizeUnderlyingOrSpoilerItself(buffer, spoiler.start, spoiler.end, QUOTE_SPAN_FLAG, spoiler)
        doColorizeUnderlyingOrSpoilerItself(buffer, spoiler.start, spoiler.end, LINK_HIGHLIGHTED_FLAG, spoiler)
    }

    fun doColorizeUnderlyingOrSpoilerItself(spannable: Spannable, spoilerStart: Int, spoilerEnd: Int,
                                            spanToFind: Int, spoiler: SpoilerSpan) {
        val underlyingSpans: Array<out CharacterStyle> =
                findUnderlyingSpans(spannable, spanToFind, spoilerStart, spoilerEnd)
        if (underlyingSpans.isNotEmpty()) {
            spoiler.enabled = true
            if (spoiler.clicked) {
                spoiler.enabled = true
                underlyingSpans.forEach {
                    val spoilerStartIsLonger = spoilerStart <= spannable.getSpanStart(it)
                    val spoilerEndIsLonger = spoilerEnd >= spannable.getSpanEnd(it)
                    val startToSpan: Int
                    val endToSpan: Int
                    if (spoilerStartIsLonger) startToSpan = spannable.getSpanStart(it)
                    else startToSpan = spoilerStart
                    if (spoilerEndIsLonger) endToSpan = spannable.getSpanEnd(it)
                    else endToSpan = spoilerEnd

                    when (spanToFind) {
                        SPOILER_SPAN_FLAG -> {
                            linkMovementMethod.setSpoilerSpan(spannable, startToSpan, endToSpan)
                        }
                        QUOTE_SPAN_FLAG -> {
                            linkMovementMethod.setQuoteSpan(spannable, startToSpan, endToSpan)
                        }
                        LINK_SPAN_FLAG -> {
                            linkMovementMethod.setLinkSpan(spannable, startToSpan, endToSpan)
                        }
                        LINK_HIGHLIGHTED_FLAG -> {
                            linkMovementMethod.setLinkHighlightedSpan(spannable, startToSpan, endToSpan)
                        }
                    }
                }
            } else {
                underlyingSpans.forEach {
                    val spoilerStartIsLonger = spoilerStart <= spannable.getSpanStart(it)
                    val spoilerEndIsLonger = spoilerEnd >= spannable.getSpanEnd(it)
                    val startToSpan: Int
                    val endToSpan: Int
                    if (spoilerStartIsLonger) startToSpan = spannable.getSpanStart(it)
                    else startToSpan = spoilerStart
                    if (spoilerEndIsLonger) endToSpan = spannable.getSpanEnd(it)
                    else endToSpan = spoilerEnd

                    linkMovementMethod.setSpoilerSpan(spannable, startToSpan, endToSpan)
                }
            }
        }
    }
}