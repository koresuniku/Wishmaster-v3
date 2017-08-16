package com.koresuniku.wishmaster.ui.text

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.koresuniku.wishmaster.R

class LinkHighlightSpan(val mContext: Context) : ClickableSpan() {
    override fun onClick(p0: View?) {

    }

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)

        ds!!.bgColor = mContext.resources.getColor(R.color.colorLinkHighlighted)
    }
}