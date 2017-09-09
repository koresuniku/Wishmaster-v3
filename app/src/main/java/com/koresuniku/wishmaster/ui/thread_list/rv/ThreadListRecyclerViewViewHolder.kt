package com.koresuniku.wishmaster.ui.thread_list.rv

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.PreferenceUtils
import com.koresuniku.wishmaster.domain.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.text.SpanTagHandlerCompat
import com.koresuniku.wishmaster.ui.text.comment_link_movement_method.CommentLinkMovementMethod
import com.pixplicity.htmlcompat.HtmlCompat
import org.jetbrains.anko.dimen
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.topPadding

class ThreadListRecyclerViewViewHolder(private val mContext: Context,
        private val mAdapter: ThreadListRecyclerViewAdapter, itemView: View?) :
        BaseRecyclerViewViewHolder(itemView) {
    @BindView (R.id.thread_item_container) lateinit var mItemContainer: RelativeLayout
    @BindView (R.id.post_subject) lateinit var mSubjectTextView: TextView
    @BindView (R.id.answers) lateinit var mPostsAndFilesInfo: TextView

    init { ButterKnife.bind(this, itemView!!) }

    fun provideFiles(files: List<Files>) { this.files = files }

    fun setOnClickListener(threadNum: String) {
        mItemContainer.setOnClickListener { mAdapter.openThread(threadNum) }
    }

    override fun onThumbnailClicked(file: Files) {
        mAdapter.mGalleryUnit.showImageOrVideo(files, file)
    }

    fun setImages() {
        ListViewAdapterUtils.setupImagesForMultipleItem(this, mAdapter.getActivity(), this, false, true)
    }

    fun setNoSubject() {
        mSubjectTextView.visibility = View.GONE
        mItemContainer.topPadding = mContext.dimen(R.dimen.post_item_side_padding)
    }

    fun setHasSubject(subject: String) {
        mSubjectTextView.visibility = View.VISIBLE
        mSubjectTextView.text = Html.fromHtml(subject)
        mItemContainer.topPadding = mContext.dimen(R.dimen.post_item_side_padding) / 2
    }

    fun setMaxLines() {
        val maxLines = PreferenceUtils.getSharedPreferences(mContext).getString(
                mContext.getString(R.string.pref_lines_count_key),
                mContext.getString(R.string.pref_lines_count_default)).toInt()
        if (maxLines == 0) mCommentTextView.maxLines = Int.MAX_VALUE
        else mCommentTextView.maxLines = maxLines
    }

    fun setComment(commentHtml: String, threadNum: String) {
        mCommentTextView.context.runOnUiThread {
            mCommentTextView.linksClickable = false
            mCommentTextView.movementMethod =
                    CommentLinkMovementMethod(mContext, mAdapter, null, threadNum)
            val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            if (files.size == 1) {
                params.addRule(RelativeLayout.BELOW, mSubjectTextView.id)
                mCommentTextView.layoutParams = params
                imagesAndSummariesContainer.bringToFront()
                doAsync { ListViewAdapterUtils.setCommentSpannableForItemSingleImage(
                            mAdapter.getActivity(),
                            this@ThreadListRecyclerViewViewHolder, commentHtml, false)
                }
            } else {
                params.addRule(RelativeLayout.BELOW, imagesAndSummariesContainer.id)
                mCommentTextView.layoutParams = params
                mCommentTextView.text = HtmlCompat.fromHtml(
                        mContext, commentHtml, 0, null, SpanTagHandlerCompat(mContext))
            }
        }
    }

    fun setPostsAndFilesTextView(postsAndFiles: String) {
        mPostsAndFilesInfo.context.runOnUiThread {
            mPostsAndFilesInfo.text = postsAndFiles
        }
    }


}