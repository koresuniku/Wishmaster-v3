package com.koresuniku.wishmaster.ui.text

import android.text.Editable
import android.text.Html
import android.util.Log
import org.xml.sax.XMLReader


class SpanTagHandler : Html.TagHandler {
    val LOG_TAG: String = SpanTagHandler::class.java.simpleName

    override fun handleTag(opening: Boolean, tag: String?, text: Editable?, xmlReader: XMLReader?) {
        if (tag == SpanTagHandlerCompat.MY_SPAN_TAG) {
            Log.d(LOG_TAG, "spoiler: ${text.toString()}")
        }
    }
}