package com.koresuniku.wishmaster.ui.text

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.style.URLSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.koresuniku.wishmaster.system.Constants
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommentLinkMovementMethodPresenter(val linkMovementMethod: ICommentLinkMovementMethod) :
        ICommentLinkMovementMethodPresenter {
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

                val linkSpans = spannable.getSpans(spoilerStart, spoilerEnd, URLSpan::class.java)
                if (linkSpans.isNotEmpty()) {
                    linkSpans.forEach {
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

                val quoteSpans = spannable.getSpans(spoilerStart, spoilerEnd, QuoteSpan::class.java)
                if (quoteSpans.isNotEmpty()) {
                    quoteSpans.forEach {
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
        val isLinkFound = findLinks(widget, off, buffer, allowPerformClick = false)
        val isSpoilerFound = findSpoilers(off, buffer, widget, doClickSpoiler = false)
        if (!isLinkFound && !isSpoilerFound) {
            widget.postDelayed({
                if (isPressed) {
                    isLongPressed = true
                    linkMovementMethod.onLongClickNoSpoilersOrLinksFound()
                } else isLongPressed = false
            }, Constants.LONG_CLICK_TIME)
        }
        if (isLinkFound &&
                if (findSpoiler(off, buffer) == null) false
                else findSpoiler(off, buffer)!!.clicked) {
            widget.postDelayed({
                if (isPressed) {
                    isLongPressed = true
                    Log.d(LOG_TAG, "link long pressed: $currentLink")
                } else {
                    isLongPressed = false
                    Log.d(LOG_TAG, "is pressed was false")
                }
            }, Constants.LONG_CLICK_TIME)
        } else Log.d(LOG_TAG, "link isnt found")
    }

    override fun onActionUp(widget: TextView, buffer: Spannable, event: MotionEvent) {
        isPressed = false
        onActionUpTime = System.currentTimeMillis()
        clickTime = onActionUpTime - onActionDownTime

        val off = locateOffset(widget, event)
        if (!findLinks(widget, off, buffer, allowPerformClick = true) &&
                !findSpoilers(off, buffer, widget, doClickSpoiler = true)) {
            if (!isLongPressed) linkMovementMethod.onClickNoSpoilersOrLinksFound()
            else isLongPressed = false
        }

    }

    override fun onActionCancel(widget: TextView, buffer: Spannable, event: MotionEvent) {

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
                            it.enabled = false
                        } else {
                            it.enabled = true
                            clickSpoilerAndColorizeUnderlying(widget, buffer, it)
                            returnBecauseOfSpoiler = true
                        }
                    }
                    if (returnBecauseOfSpoiler) return true
                }
            }

            var pattern: Pattern = Pattern.compile("/[a-z]+/res/[0-9]+[.]html#[0-9]+")
            var matcher: Matcher = pattern.matcher(links[0].url)

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

            pattern = Pattern.compile("/[a-z]+/res/[0-9]+[.]html")
            matcher = pattern.matcher(links[0].url)

            if (matcher.find()) {
                //TODO: handle inner 2ch link
                currentLink = matcher.group()
                Log.d(LOG_TAG, "inner 2ch link: ${matcher.group()}")

                return true
            }

            pattern = Pattern.compile("https?://2ch\\.hk.*")
            matcher = pattern.matcher(links[0].url)

            if (matcher.find()) {
                //TODO: handle general 2ch link
                currentLink = matcher.group()
                Log.d(LOG_TAG, "outer 2ch link: ${matcher.group()}")
                return true
            }

            pattern = Pattern.compile("https?://.+")
            matcher = pattern.matcher(links[0].url)

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


        }

        return false
    }

    fun findSpoilers(off: Int, buffer: Spannable?, widget: TextView?, doClickSpoiler: Boolean): Boolean {
        val spoilers = buffer!!.getSpans(off, off, SpoilerSpan::class.java)
        if (spoilers.isNotEmpty()) {
            //Log.d(LOG_TAG, "found spoiler span:")
            val spoiler = spoilers[0]
            if (doClickSpoiler) clickSpoilerAndColorizeUnderlying(widget!!, buffer, spoiler)
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

        spoiler.onClick(widget)
        //Log.d(LOG_TAG, "SPOILER SUKA CLICKED!!!")

        val linkSpans = buffer.getSpans(spoiler.start, spoiler.end, URLSpan::class.java)
        if (linkSpans.isNotEmpty()) {
            //Log.d(LOG_TAG, "found ${linkSpans.size} links spans")
            spoiler.enabled = true
            if (spoiler.clicked) {
                //Log.d(LOG_TAG, "SPOILER SUKA CLICKED!!!")
                linkSpans.forEach {
                    linkMovementMethod.setLinkSpan(
                            buffer, buffer.getSpanStart(it), buffer.getSpanEnd(it))
                }
            } else {
                linkSpans.forEach {
                    linkMovementMethod.setSpoilerSpan(
                            buffer, buffer.getSpanStart(it), buffer.getSpanEnd(it))
                }
            }
        }

        val quoteSpans = buffer.getSpans(spoiler.start, spoiler.end, QuoteSpan::class.java)
        if (quoteSpans.isNotEmpty()) {
            //Log.d(LOG_TAG, "found ${linkSpans.size} quote spans")
            val spoilerStart = spoiler.start
            val spoilerEnd = spoiler.end

            if (spoiler.clicked) {
                //Log.d(LOG_TAG, "SPOILER SUKA CLICKED!!!")
                spoiler.enabled = true
                quoteSpans.forEach {
                    val spoilerStartIsLonger = spoilerStart <= buffer.getSpanStart(it)
                    val spoilerEndIsLonger = spoilerEnd >= buffer.getSpanEnd(it)
                    val startToSpan: Int
                    val endToSpan: Int
                    if (spoilerStartIsLonger) startToSpan = buffer.getSpanStart(it)
                    else startToSpan = spoilerStart
                    if (spoilerEndIsLonger) endToSpan = buffer.getSpanEnd(it)
                    else endToSpan = spoilerEnd

                    linkMovementMethod.setQuoteSpan(buffer, startToSpan, endToSpan)
                }
            } else {
                quoteSpans.forEach {
                    val spoilerStartIsLonger = spoilerStart <= buffer.getSpanStart(it)
                    val spoilerEndIsLonger = spoilerEnd >= buffer.getSpanEnd(it)
                    val startToSpan: Int
                    val endToSpan: Int
                    if (spoilerStartIsLonger) startToSpan = buffer.getSpanStart(it)
                    else startToSpan = spoilerStart
                    if (spoilerEndIsLonger) endToSpan = buffer.getSpanEnd(it)
                    else endToSpan = spoilerEnd

                    linkMovementMethod.setSpoilerSpan(buffer, startToSpan, endToSpan)
                }
            }
        }

        val linkHighlightSpans = buffer.getSpans(spoiler.start, spoiler.end, LinkHighlightSpan::class.java)
        if (linkHighlightSpans.isNotEmpty()) {
            val spoilerStart = spoiler.start
            val spoilerEnd = spoiler.end

            if (spoiler.clicked) {
                spoiler.enabled = true
                linkHighlightSpans.forEach {
                    val spoilerStartIsLonger = spoilerStart <= buffer.getSpanStart(it)
                    val spoilerEndIsLonger = spoilerEnd >= buffer.getSpanEnd(it)
                    val startToSpan: Int
                    val endToSpan: Int
                    if (spoilerStartIsLonger) startToSpan = buffer.getSpanStart(it)
                    else startToSpan = spoilerStart
                    if (spoilerEndIsLonger) endToSpan = buffer.getSpanEnd(it)
                    else endToSpan = spoilerEnd

                    linkMovementMethod.setLinkHighlightedSpan(buffer, startToSpan, endToSpan)
                }
            } else {
                linkHighlightSpans.forEach {
                    val spoilerStartIsLonger = spoilerStart <= buffer.getSpanStart(it)
                    val spoilerEndIsLonger = spoilerEnd >= buffer.getSpanEnd(it)
                    val startToSpan: Int
                    val endToSpan: Int
                    if (spoilerStartIsLonger) startToSpan = buffer.getSpanStart(it)
                    else startToSpan = spoilerStart
                    if (spoilerEndIsLonger) endToSpan = buffer.getSpanEnd(it)
                    else endToSpan = spoilerEnd

                    linkMovementMethod.setSpoilerSpan(buffer, startToSpan, endToSpan)
                }
            }
        }
    }
}