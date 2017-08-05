package com.koresuniku.wishmaster.ui.text

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
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersManager
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommentLinkMovementMethod(val answersManager: AnswersManager) : LinkMovementMethod() {
    val LOG_TAG: String = CommentLinkMovementMethod::class.java.simpleName

    val backgroundColorSpan: BackgroundColorSpan = BackgroundColorSpan(R.color.linkBackground)

    override fun initialize(widget: TextView?, text: Spannable?) {
        super.initialize(widget, text)
        findQuote(widget, text)
    }

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        val action = event!!.action

        Log.d(LOG_TAG, "action: $action")

        if (action == MotionEvent.ACTION_DOWN) {
            val off = locateOffset(widget, event)
            //findLink(off, buffer, true)
            return true
        }
        if (action == MotionEvent.ACTION_UP) {
            val off = locateOffset(widget, event)
            findLink(off, buffer, false)
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

    fun findLink(off: Int, buffer: Spannable?, highlightLinkAndDoNotOpenPost: Boolean) {
        // Find the URL that was pressed
        val links = buffer!!.getSpans(off, off, URLSpan::class.java)

        if (links.isNotEmpty()) {
            Log.d(LOG_TAG, "links: ${links[0].url}")
            var pattern: Pattern = Pattern.compile("/[a-z]+/res/[0-9]+[.]html#[0-9]+")
            var matcher: Matcher = pattern.matcher(links[0].url)

            if (matcher.find()) {
                pattern = Pattern.compile("#[0-9]+")
                matcher = pattern.matcher(matcher.group())
                Log.d(LOG_TAG, "highlightLinkAndDoNotOpenPost: $highlightLinkAndDoNotOpenPost")
                if (matcher.find() && !highlightLinkAndDoNotOpenPost) {
                    answersManager.openSingleAnswer(
                            matcher.group().substring(1, matcher.group().length))
                }
                if (highlightLinkAndDoNotOpenPost) highlightLink(buffer, links[0])
                else unhighlightLink(buffer)
            }
        }
    }

    fun highlightLink(buffer: Spannable?, span: URLSpan) {
        buffer!!.setSpan(backgroundColorSpan, buffer.getSpanStart(span), buffer.getSpanEnd(span), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun unhighlightLink(buffer: Spannable) {
        buffer.removeSpan(backgroundColorSpan)
    }

    fun findQuote(widget: TextView?, buffer: Spannable?) {
        val lines = widget!!.text.lines()
        Log.d(LOG_TAG, "lines type: ${lines.javaClass.canonicalName}")

        if (lines.isEmpty()) return

        var start: Int = 0
        var end: Int = lines[0].length

        val pattern: Pattern = Pattern.compile("^>[^>]+")
        var matcher: Matcher

        lines.forEach { if (lines.indexOf(it) != 0) { start = end + 1; end += it.length + 1 }
            matcher = pattern.matcher(it)
            if (matcher.find()) { buffer!!.setSpan(ForegroundColorSpan(
                        widget.resources.getColor(R.color.colorQuote)),
                        start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }


}