package com.koresuniku.wishmaster.ui.text.comment_leading_margin_span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.view.View
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.UiUtils
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats
import org.jetbrains.anko.dimen

class CommentLeadingMarginSpan2(val every: Int) :
        LeadingMarginSpan.LeadingMarginSpan2 {
    val LOG_TAG: String = CommentLeadingMarginSpan2::class.java.simpleName

    companion object {
        val LOG_TAG: String = CommentLeadingMarginSpan2::class.java.simpleName

//        fun computeLinesToBeSpanned(holder: CommentAndFilesListViewViewHolder): Int {
//            val imageContainerHeightInDp = calculateImageContainerHeightInDp(holder)
//            val commentLineHeightInDp = computeCommentLineHeightInDp(holder)
//            return Math.ceil((imageContainerHeightInDp / commentLineHeightInDp).toDouble()).toInt()
//        }

        fun calculateImageContainerHeightInDp(holder: FilesListViewViewHolder, forDialog: Boolean): Float {
            val containerView = holder.imageAndSummaryContainer
            val imageViewHeight = ListViewAdapterUtils
                    .computeImageHeightInPx(holder.activity, holder.files!![0], forDialog)
            val summaryLineHeight = holder.summary!!.lineHeight
            val summaryHeight = if (Formats.VIDEO_FORMATS.contains(
                    TextUtils.getSubstringAfterDot(holder.files!![0].getPath()!!)))
                summaryLineHeight * 3 else summaryLineHeight * 2
            val additionalMarginBottom =
                    containerView!!.context.dimen(R.dimen.post_item_text_margin_flow).toFloat()
            return UiUtils.convertPixelsToDp(imageViewHeight + summaryHeight + additionalMarginBottom)
        }

//        fun computeCommentLineHeightInDp(holder: CommentAndFilesListViewViewHolder): Float {
//            val commentTextView = holder.mCommentTextView
//            return UiUtils.convertPixelsToDp(commentTextView!!.lineHeight.toFloat())
//        }

        fun calculateLeadingMarginWidthInPx(holder: FilesListViewViewHolder): Int {
            holder.imageAndSummaryContainer!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return holder.imageAndSummaryContainer!!.measuredWidth +
                    holder.imageAndSummaryContainer!!.context.dimen(R.dimen.post_item_side_padding)
        }

        fun calculateCommentTextViewWidthInPx(holder: FilesListViewViewHolder,
                                              forDialog: Boolean): Int {
            val commentTextView = holder.mCommentTextView!!
            commentTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val displayWidth = holder.activity.windowManager.defaultDisplay.width
            val marginWidth = calculateLeadingMarginWidthInPx(holder)
            val paddingWidth = holder.imageAndSummaryContainer!!.context.dimen(R.dimen.post_item_side_padding)
            val dialogPadding = UiUtils.convertDpToPixel(16.0f).toInt()
            return displayWidth - (paddingWidth * 2) - marginWidth - if (forDialog) (dialogPadding * 2) else 0
        }
    }

    override fun drawLeadingMargin(c: Canvas?, p: Paint?, x: Int, dir: Int, top: Int, baseline: Int,
                                   bottom: Int, text: CharSequence?, start: Int, end: Int,
                                   first: Boolean, layout: Layout?) {}

    override fun getLeadingMargin(first: Boolean): Int {
        return every
    }


    override fun getLeadingMarginLineCount(): Int {
        return Int.MAX_VALUE
    }
}