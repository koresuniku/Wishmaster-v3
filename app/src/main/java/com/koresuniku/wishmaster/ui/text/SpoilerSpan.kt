package com.koresuniku.wishmaster.ui.text

import android.content.Context
import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import com.koresuniku.wishmaster.R

class SpoilerSpan(mContext: Context, val start: Int, val end: Int) : ClickableSpan() {
    val LOG_TAG: String = SpoilerSpan::class.java.simpleName

    var clicked: Boolean = false
    var enabled: Boolean = true
    val color: Int = mContext.resources.getColor(R.color.colorSpoiler)

    override fun onClick(view: View?) {
        Log.d(LOG_TAG, "onClick: enabled: $enabled, clicked: $clicked")
        if (enabled) {
            clicked = !clicked
            view!!.invalidate()
            updateDrawState(TextPaint())
        }
    }

    override fun updateDrawState(ds: TextPaint?) {
        ds!!.bgColor = color
        if (clicked) ds.color = Color.BLACK
        else ds.color = Color.TRANSPARENT
    }
}