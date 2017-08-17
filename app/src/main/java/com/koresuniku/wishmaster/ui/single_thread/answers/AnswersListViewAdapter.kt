package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Activity
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewAdapter
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewView
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.find
import java.util.regex.Matcher
import java.util.regex.Pattern

class AnswersListViewAdapter(mView: SingleThreadListViewView,
                             mSchema: BaseJsonSchemaImpl,
                             override var mAnswersManager: AnswersManager,
                             private val mPostWhomAnswered: String?) :
        SingleThreadListViewAdapter(mView, mSchema, true) {
    override val LOG_TAG: String = AnswersListViewAdapter::class.java.simpleName

    override fun initAnswersManager() {}

    override fun onThumbnailClickedEvent(event: OnThumbnailClickedEvent) {}

    override fun getCount(): Int = mSchema.getPosts()!!.size

    override fun getActivity(): Activity = mView.getActivity()

    override fun postGetView(holder: ViewHolderAndFiles) {
        if (!mPostWhomAnswered.isNullOrEmpty()) {
            val spannable = SpannableString(holder.mCommentTextView!!.text)
            holder.mCommentTextView!!.text = doSpanComment(spannable, mPostWhomAnswered!!)
        }

    }

    override fun onThumbnailClicked(file: Files) {
        EventBus.getDefault().post(OnThumbnailClickedEvent(file))
    }

    private fun doSpanComment(comment: SpannableString, number: String): SpannableString {
        val pattern: Pattern = Pattern.compile(">>$number")
        val matcher: Matcher = pattern.matcher(comment)

        var start: Int
        var end: Int
        while (matcher.find()) {
            start = matcher.start()
            end = matcher.end()
            if (end + 5 <= comment.length && comment.substring(end, end + 5) == " (OP)") end += 5
            comment.setSpan(StyleSpan(Typeface.BOLD),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            comment.setSpan(ForegroundColorSpan(mView.getActivity().resources.getColor(R.color.colorLinkHighlighted)),
//                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return comment
    }
}