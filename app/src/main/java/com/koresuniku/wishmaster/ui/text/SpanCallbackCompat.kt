package com.koresuniku.wishmaster.ui.text

import android.text.Html
import android.util.Log
import com.pixplicity.htmlcompat.HtmlCompat

/**
 * Created by koresuniku on 06.08.17.
 */
class SpanCallbackCompat : HtmlCompat.SpanCallback {
    val LOG_TAG: String = SpanCallbackCompat::class.java.simpleName

    override fun onSpanCreated(p0: String?, p1: Any?): Any {
        Log.d(LOG_TAG, "onSpanCreated: $p0")
        return Any()
    }
}