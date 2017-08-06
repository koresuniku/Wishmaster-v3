package com.koresuniku.wishmaster.ui.text

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.controller.ClickableAdapter
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersManager
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommentLinkMovementMethod(val mContext: Context, val answersManager: AnswersManager?) : LinkMovementMethod() {
    val LOG_TAG: String = CommentLinkMovementMethod::class.java.simpleName

    val backgroundColorSpan: BackgroundColorSpan = BackgroundColorSpan(R.color.linkBackground)

    var mClickableAdapter: ClickableAdapter? = null
    var mThreadNumber: String? = null

    constructor(mContext: Context, clickableAdapter: ClickableAdapter, threadNumber: String) :
            this(mContext, null) {
        mClickableAdapter = clickableAdapter
        mThreadNumber = threadNumber
    }

    override fun initialize(widget: TextView?, text: Spannable?) {
        super.initialize(widget, text)
        initFixSpoilerSpans(text!!)
    }

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        val action = event!!.action

        if (action == MotionEvent.ACTION_DOWN) {
            return true
        }
        if (action == MotionEvent.ACTION_UP) {
            val off = locateOffset(widget, event)
            if (!(findLink(widget!!, off, buffer, false) || findSpoilerSpan(off, buffer, widget))) {
                mClickableAdapter?.onClick(mThreadNumber!!)
            }
            return true
        }
        if (action == MotionEvent.ACTION_CANCEL) {
            unhighlightLink(buffer!!)
            return true
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    fun locateOffset(widget: TextView?, event: MotionEvent): Int {
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= widget!!.totalPaddingLeft
        y -= widget.totalPaddingTop
        x += widget.scrollX
        y += widget.scrollY
        val layout = widget.layout
        val line = layout.getLineForVertical(y)
        return layout.getOffsetForHorizontal(line, x.toFloat())
    }

    fun findLink(widget: TextView, off: Int, buffer: Spannable?, highlightLinkAndDoNotOpenPost: Boolean): Boolean {
        // Find the URL that was pressed
        val links = buffer!!.getSpans(off, off, URLSpan::class.java)

        if (links.isNotEmpty()) {
            Log.d(LOG_TAG, "found link: ${links[0].url}")
            val spoilerSpans = buffer.getSpans(off, off, SpoilerSpan::class.java)
            if (spoilerSpans.isNotEmpty()) {
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

            var pattern: Pattern = Pattern.compile("/[a-z]+/res/[0-9]+[.]html#[0-9]+")
            var matcher: Matcher = pattern.matcher(links[0].url)

            if (matcher.find()) {
                pattern = Pattern.compile("#[0-9]+")
                matcher = pattern.matcher(matcher.group())
                if (matcher.find() && !highlightLinkAndDoNotOpenPost) {
                    Log.d(LOG_TAG, "2ch answer link: ${matcher.group()}")
                    answersManager?.openSingleAnswer(
                            matcher.group().substring(1, matcher.group().length))
                }
                if (highlightLinkAndDoNotOpenPost) highlightLink(buffer, links[0])
                else unhighlightLink(buffer)
                return true
            }

            pattern = Pattern.compile("https?://2ch\\.hk.*")
            matcher = pattern.matcher(links[0].url)

            if (matcher.find()) {
                //TODO: handle 2ch link
                Log.d(LOG_TAG, "outer 2ch link: ${matcher.group()}")
                return true
            }

            pattern = Pattern.compile("https?://.+")
            matcher = pattern.matcher(links[0].url)

            if (matcher.find()) {
                Log.d(LOG_TAG, "outer link: ${matcher.group()}")
                mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(matcher.group())))
            }

        }

        return false
    }

    fun clickSpoilerAndColorizeUnderlying(widget: TextView, buffer: Spannable, spoiler: SpoilerSpan) {
        spoiler.onClick(widget)

        val linkSpans = buffer.getSpans(spoiler.start, spoiler.end, URLSpan::class.java)
        if (linkSpans.isNotEmpty()) {
            Log.d(LOG_TAG, "found ${linkSpans.size} links spans")
            spoiler.enabled = true
            if (spoiler.clicked) {
                Log.d(LOG_TAG, "SPOILER SUKA CLICKED!!!")
                linkSpans.forEach {
                    buffer.setSpan(ForegroundColorSpan(
                            mContext.resources.getColor(R.color.colorAccent)),
                            buffer.getSpanStart(it), buffer.getSpanEnd(it),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            } else {
                linkSpans.forEach {
                    buffer.setSpan(ForegroundColorSpan(
                            mContext.resources.getColor(R.color.colorSpoiler)),
                            buffer.getSpanStart(it), buffer.getSpanEnd(it),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        val quoteSpans = buffer.getSpans(spoiler.start, spoiler.end, QuoteSpan::class.java)
        if (quoteSpans.isNotEmpty()) {
            Log.d(LOG_TAG, "found ${linkSpans.size} quote spans")
            spoiler.enabled = true
            val spoilerStart = spoiler.start
            val spoilerEnd = spoiler.end

            if (spoiler.clicked) {
                Log.d(LOG_TAG, "SPOILER SUKA CLICKED!!!")
                quoteSpans.forEach {
                    val spoilerStartIsLonger = spoilerStart <= buffer.getSpanStart(it)
                    val spoilerEndIsLonger = spoilerEnd >= buffer.getSpanEnd(it)
                    val startToSpan: Int
                    val endToSpan: Int
                    if (spoilerStartIsLonger) startToSpan = buffer.getSpanStart(it)
                    else startToSpan = spoilerStart
                    if (spoilerEndIsLonger) endToSpan = buffer.getSpanEnd(it)
                    else endToSpan = spoilerEnd

                    buffer.setSpan(ForegroundColorSpan(
                            mContext.resources.getColor(R.color.colorQuote)),
                            startToSpan, endToSpan,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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

                    buffer.setSpan(ForegroundColorSpan(
                            mContext.resources.getColor(R.color.colorSpoiler)),
                            startToSpan, endToSpan,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }

    fun findSpoilerSpan(off: Int, buffer: Spannable?, widget: TextView?): Boolean {
        val spoilers = buffer!!.getSpans(off, off, SpoilerSpan::class.java)

        if (spoilers.isNotEmpty()) {
            Log.d(LOG_TAG, "found spoiler span:")
            val spoiler = spoilers[0]
            clickSpoilerAndColorizeUnderlying(widget!!, buffer, spoiler)

            return true
        } else Log.d(LOG_TAG, "no spoiler spans found")

        return false
    }

    fun initFixSpoilerSpans(spannable: Spannable) {
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

                        spannable.setSpan(ForegroundColorSpan(
                                mContext.resources.getColor(R.color.colorSpoiler)),
                                startToSpan, endToSpan,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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

                        spannable.setSpan(ForegroundColorSpan(
                                mContext.resources.getColor(R.color.colorSpoiler)),
                                startToSpan, endToSpan,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
        }
    }

    fun highlightLink(buffer: Spannable?, span: URLSpan) {
        buffer!!.setSpan(backgroundColorSpan, buffer.getSpanStart(span),
                buffer.getSpanEnd(span), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun unhighlightLink(buffer: Spannable) {
        buffer.removeSpan(backgroundColorSpan)
    }

//    fun findQuote(widget: TextView?, buffer: Spannable?) {
//        val lines = widget!!.text.lines()
//
//        if (lines.isEmpty()) return
//
//        var start: Int = 0
//        var end: Int = lines[0].length
//
//        val pattern: Pattern = Pattern.compile("^>[^>]+")
//        var matcher: Matcher
//
//        var line: String
//        for (i in 0..lines.size - 1) {
//            line = lines[i]
//            if (i != 0) { start = end + 1; end += line.length + 1 }
//            matcher = pattern.matcher(line)
//            if (matcher.find()) { buffer!!.setSpan(ForegroundColorSpan(
//                    widget.resources.getColor(R.color.colorQuote)),
//                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
//        }
//    }


}