package com.koresuniku.wishmaster.ui.text

import android.content.Context
import android.text.Editable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.koresuniku.wishmaster.R
import com.pixplicity.htmlcompat.HtmlCompat
import org.xml.sax.Attributes
import org.xml.sax.XMLReader
import android.text.style.BackgroundColorSpan
import android.widget.Toast
import java.util.regex.Matcher
import java.util.regex.Pattern


class SpanTagHandlerCompat(val mContext: Context) : HtmlCompat.TagHandler {
    val LOG_TAG: String = SpanTagHandlerCompat::class.java.simpleName

    companion object {
        val SPAN_TAG: String = "span"
        val MY_SPAN_TAG: String = "myspan"
        val QUOTE_TAG: String = "quote"
        val SPOILER_TAG: String = "spoiler"

        val CLASS_ATTR: String = "class"

        val SPOILER_VALUE: String = "spoiler"
        val QUOTE_VALUE: String = "unkfunc"
    }

    var quoteStart: Int = 0
    var quoteEnd: Int = 0
    var spoilerStart: Int = 0
    var spoilerEnd: Int = 0

    override fun handleTag(opening: Boolean, tag: String?, attrs: Attributes?, output: Editable?, xmlReader: XMLReader?) {

        if (tag == QUOTE_TAG) {
            if (opening) {
                quoteStart = output!!.length
            } else {
                quoteEnd = output!!.length
                doSpanQuote(output, quoteStart, quoteEnd)
                quoteStart = 0
                quoteEnd = 0
            }
        }

        if (tag == SPOILER_TAG) {
            if (opening) {
                spoilerStart = output!!.length
            } else {
                spoilerEnd = output!!.length - 1
                doSpanSpoiler(output, spoilerStart, spoilerEnd)
                spoilerStart = 0
                spoilerEnd = 0
            }
        }
    }

    fun doSpanQuote(text: Editable, start: Int, end: Int) {
        if (end - start > 0) {
            text.setSpan(QuoteSpan(mContext.resources.getColor(R.color.colorQuote)),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    fun doSpanSpoiler(text: Editable, start: Int, end: Int) {
        if (end - start > 0) {
            text.setSpan(SpoilerSpan(mContext, start, end), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}