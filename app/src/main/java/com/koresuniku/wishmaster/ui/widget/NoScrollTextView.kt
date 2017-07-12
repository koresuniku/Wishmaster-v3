package com.koresuniku.wishmaster.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Spannable
import android.util.AttributeSet
import android.widget.TextView

class NoScrollTextView : TextView {


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun scrollTo(x: Int, y: Int) {
        //do nothing
    }


}