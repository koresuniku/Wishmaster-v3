package com.koresuniku.wishmaster.ui.text

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.koresuniku.wishmaster.ui.single_thread.AnswersHolder
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommentLinkMovementMethod(val answersHolder: AnswersHolder) : LinkMovementMethod() {
    val LOG_TAG: String = CommentLinkMovementMethod::class.java.simpleName

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        val action = event!!.action

        if (action == MotionEvent.ACTION_DOWN) {
            val off = locateOffset(widget, event)

            findLinks(off, buffer)

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

    fun findLinks(off: Int, buffer: Spannable?) {
        // Find the URL that was pressed
        val links = buffer!!.getSpans(off, off, URLSpan::class.java)

        if (links.isNotEmpty()) {
            Log.d(LOG_TAG, "links: ${links[0].url}")
            var pattern: Pattern = Pattern.compile("/[a-z]+/res/[0-9]+[.]html#[0-9]+")
            var matcher: Matcher = pattern.matcher(links[0].url)

            if (matcher.find()) {
                pattern = Pattern.compile("#[0-9]+")
                matcher = pattern.matcher(matcher.group())
                if (matcher.find()) {
                    answersHolder.openSigleAnswer(
                            matcher.group().substring(1, matcher.group().length))
                }
            }
        }
    }
}