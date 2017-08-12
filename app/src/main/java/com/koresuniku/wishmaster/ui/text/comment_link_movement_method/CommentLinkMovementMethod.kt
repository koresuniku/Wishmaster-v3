package com.koresuniku.wishmaster.ui.text.comment_link_movement_method

import android.content.Context
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.controller.ClickableAdapter
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersManager
import com.koresuniku.wishmaster.ui.text.LinkHighlightSpan

class CommentLinkMovementMethod(val mContext: Context, val answersManager: AnswersManager?) :
        LinkMovementMethod(), ICommentLinkMovementMethod {
    val LOG_TAG: String = CommentLinkMovementMethod::class.java.simpleName

    val presenter: ICommentLinkMovementMethodPresenter = CommentLinkMovementMethodPresenter(this)

    var mClickableAdapter: ClickableAdapter? = null
    var mThreadNumber: String? = null

    constructor(mContext: Context, clickableAdapter: ClickableAdapter, threadNumber: String) :
            this(mContext, null) {
        mClickableAdapter = clickableAdapter
        mThreadNumber = threadNumber
    }

    override fun initialize(widget: TextView?, text: Spannable?) {
        super.initialize(widget, text)
        presenter.initFixSpoilerSpans(text!!)
    }

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        val action = event!!.action

        if (action == MotionEvent.ACTION_DOWN) {
            Log.d(LOG_TAG, "action_down")
            presenter.onActionDown(widget!!, buffer!!, event)
            return true
        }
        if (action == MotionEvent.ACTION_UP) {
            Log.d(LOG_TAG, "action_up")
            presenter.onActionUp(widget!!, buffer!!, event)
            return true
        }
        if (action == MotionEvent.ACTION_CANCEL) {
            Log.d(LOG_TAG, "action_cancel")
            presenter.onActionCancel(widget!!, buffer!!, event)
            return true
        }
        return false
    }

    override fun getContext(): Context {
        return mContext
    }

    override fun onClickNoSpoilersOrLinksFound() {
        mClickableAdapter?.onClick(mThreadNumber!!)
    }

    override fun onLongClickNoSpoilersOrLinksFound() {
        mClickableAdapter?.onLongClick(mThreadNumber!!)
    }

    override fun on2chAnswerLinkClicked(number: String) {
        answersManager?.openSingleAnswer(number)
    }

    override fun setSpoilerSpan(buffer: Spannable, start: Int, end: Int) {
        buffer.setSpan(ForegroundColorSpan(
                mContext.resources.getColor(R.color.colorSpoiler)), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    override fun setQuoteSpan(buffer: Spannable, start: Int, end: Int) {
        buffer.setSpan(ForegroundColorSpan(
                mContext.resources.getColor(R.color.colorQuote)), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    override fun setLinkHighlightedSpan(buffer: Spannable, start: Int, end: Int) {
        buffer.setSpan(ForegroundColorSpan(
                mContext.resources.getColor(R.color.colorLinkHighlighted)), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    override fun setLinkSpan(buffer: Spannable, start: Int, end: Int) {
        buffer.setSpan(ForegroundColorSpan(
                mContext.resources.getColor(R.color.colorAccent)), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    override fun highlightLink(buffer: Spannable, span: URLSpan) {
        buffer.setSpan(LinkHighlightSpan(mContext),
                buffer.getSpanStart(span), buffer.getSpanEnd(span), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    override fun unhighlightLink(buffer: Spannable) {
        val linkHighlightSpans = buffer.getSpans(0, buffer.length, LinkHighlightSpan::class.java)
        if (linkHighlightSpans.isNotEmpty()) { linkHighlightSpans.forEach { buffer.removeSpan(it) } }
    }

}