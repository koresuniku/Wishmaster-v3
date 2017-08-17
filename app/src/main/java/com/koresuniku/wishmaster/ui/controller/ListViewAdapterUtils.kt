package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import android.net.Uri
import android.text.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.Dvach
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.UiUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersManager
import com.koresuniku.wishmaster.ui.text.SpanTagHandlerCompat
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.text.comment_leading_margin_span.CommentLeadingMarginSpan2
import com.koresuniku.wishmaster.ui.text.comment_link_movement_method.CommentLinkMovementMethod
import com.koresuniku.wishmaster.util.Formats
import com.pixplicity.htmlcompat.HtmlCompat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.security.acl.LastOwnerException

object ListViewAdapterUtils {
    val LOG_TAG: String = ListViewAdapterUtils::class.java.simpleName

    val ITEM_NO_IMAGES: Int = 0
    val ITEM_SINGLE_IMAGE: Int = 1
    val ITEM_MULTIPLE_IMAGES: Int = 2

    interface OnThumbnailClickedCallback {
        fun onThumbnailClicked(file: Files)
    }

    fun setupComment(holder: CommentAndFilesListViewViewHolder, post: Post, mAnswersHolder: AnswersManager, forDialog: Boolean) {
        val commentDocument: Document = Jsoup.parse(post.getComment())
        val commentElements: Elements = commentDocument.select(SpanTagHandlerCompat.SPAN_TAG)

        commentElements.forEach{ it.getElementsByAttributeValue(
                SpanTagHandlerCompat.CLASS_ATTR, SpanTagHandlerCompat.QUOTE_VALUE)
                .tagName(SpanTagHandlerCompat.QUOTE_TAG)
        }
        commentElements.forEach{
            it.getElementsByAttributeValue(
                    SpanTagHandlerCompat.CLASS_ATTR, SpanTagHandlerCompat.SPOILER_VALUE)
                    .tagName(SpanTagHandlerCompat.SPOILER_TAG)
        }

        holder.mCommentTextView!!.linksClickable = false
        holder.mCommentTextView!!.movementMethod =
                CommentLinkMovementMethod(holder.getActivity(), mAnswersHolder)

        if (holder.viewType == ListViewAdapterUtils.ITEM_SINGLE_IMAGE) {
            holder.mCommentTextView!!.post {
                var spannable = SpannableString(HtmlCompat.fromHtml(
                        holder.getActivity(), commentDocument.html(), 0,
                        null, SpanTagHandlerCompat(holder.getActivity())))
                val textViewWidth = CommentLeadingMarginSpan2.calculateCommentTextViewWidthInPx(holder)

                var end: Int = 0
                var overallHeightOfLines: Int = 0
                val imageContainerHeight: Int = UiUtils.convertDpToPixel(
                        CommentLeadingMarginSpan2.calculateImageContainerHeightInDp(holder, forDialog)).toInt()
                val commentParts = spannable.toString().split("\r")

                var endReached: Boolean = false
                commentParts.forEach {
                    if (endReached) return@forEach

                    val layout: StaticLayout = StaticLayout(it, holder.mCommentTextView!!.paint,
                            textViewWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false)
                    if (layout.lineCount > 0) {
                        var localHeight: Int

                        for (lineIndex in 0..layout.lineCount - 1) {
                            localHeight = layout.getLineBottom(lineIndex)
                            if (localHeight + overallHeightOfLines > imageContainerHeight) {
                                endReached = true
                                end = layout.getLineEnd(lineIndex)
                                val spannableStringBuilder = SpannableStringBuilder(spannable)
                                if (spannable.substring(end - 1, end) != "\n" &&
                                        spannable.substring(end - 1, end) != "\r") {
                                    if (spannable.substring(end - 1, end) == " ") {
                                        spannableStringBuilder.replace(end - 1, end, "\n")
                                    } else {
                                        spannableStringBuilder.insert(end, "\n")
                                    }
                                }
                                spannable = SpannableString(spannableStringBuilder)
                                break
                            }
                        }
                        overallHeightOfLines += layout.lineCount * holder.mCommentTextView!!.lineHeight
                    }
                }

                spannable.setSpan(CommentLeadingMarginSpan2(
                        CommentLeadingMarginSpan2.calculateLeadingMarginWidthInPx(holder)),
                        0, if (end == 0) spannable.length else end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                holder.mCommentTextView!!.text = spannable

                holder.mCommentTextView!!.requestLayout()
                holder.imageAndSummaryContainer!!.bringToFront()
            }
        } else {
            holder.mCommentTextView!!.text = HtmlCompat.fromHtml(
                    holder.getActivity(), commentDocument.html(), 0,
                    null, SpanTagHandlerCompat(holder.getActivity()))
        }
    }

    fun setupImages(callback: OnThumbnailClickedCallback, activity: Activity,
                    holder: FilesListViewViewHolder, viewModeIsDialog: Boolean,
                    reloadImages: Boolean) {
        if (holder.files == null) {Log.d("ListViewAdapterUtils", "files is null"); return}
        val filesSize = holder.files!!.size
        switchImagesVisibility(holder, filesSize)

        if (filesSize != 0) {
            for (file in holder.files!!) {
                if (filesSize == 1) {
                    setupImageContainer(callback, activity, holder, holder.image!!, holder.webmImageView!!, holder.summary!!, file, viewModeIsDialog, reloadImages)
                }
                if (filesSize > 1) {
                    when (holder.files!!.indexOf(file)) {
                        0 -> setupImageContainer(callback, activity, holder, holder.image1!!, holder.webmImageView1!!,
                                holder.summary1!!, file, viewModeIsDialog, reloadImages)
                        1 -> setupImageContainer(callback, activity, holder, holder.image2!!, holder.webmImageView2!!,
                                holder.summary2!!, file, viewModeIsDialog, reloadImages)
                        2 -> setupImageContainer(callback, activity, holder, holder.image3!!, holder.webmImageView3!!,
                                holder.summary3!!, file, viewModeIsDialog, reloadImages)
                        3 -> setupImageContainer(callback, activity, holder, holder.image4!!, holder.webmImageView4!!,
                                holder.summary4!!, file, viewModeIsDialog, reloadImages)
                        4 -> setupImageContainer(callback, activity, holder, holder.image5!!, holder.webmImageView5!!,
                                holder.summary5!!, file, viewModeIsDialog, reloadImages)
                        5 -> setupImageContainer(callback, activity, holder, holder.image6!!, holder.webmImageView6!!,
                                holder.summary6!!, file, viewModeIsDialog, reloadImages)
                        6 -> setupImageContainer(callback, activity, holder, holder.image7!!, holder.webmImageView7!!,
                                holder.summary7!!, file, viewModeIsDialog, reloadImages)
                        7 -> setupImageContainer(callback, activity, holder, holder.image8!!, holder.webmImageView8!!,
                                holder.summary8!!, file, viewModeIsDialog, reloadImages)
                    }
                }
            }
        }
    }

    fun switchImagesVisibility(holder: FilesListViewViewHolder, filesSize: Int) {
        switchImagesVisibility(
                holder.imageAndSummaryContainer1, holder.imageAndSummaryContainer2,
                holder.imageAndSummaryContainer3, holder.imageAndSummaryContainer4,
                holder.imageAndSummaryContainer5, holder.imageAndSummaryContainer6,
                holder.imageAndSummaryContainer7, holder.imageAndSummaryContainer8,
                filesSize)
    }

    fun switchImagesVisibility(
            imageAndSummaryContainer1: View?, imageAndSummaryContainer2: View?,
            imageAndSummaryContainer3: View?, imageAndSummaryContainer4: View?,
            imageAndSummaryContainer5: View?, imageAndSummaryContainer6: View?,
            imageAndSummaryContainer7: View?, imageAndSummaryContainer8: View?,
            filesSize: Int) {
        when (filesSize) {
            1 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.GONE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.GONE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.GONE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.GONE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.GONE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.GONE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.GONE
            }
            2 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.VISIBLE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.GONE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.GONE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.GONE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.GONE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.GONE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.GONE
            }
            3 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.VISIBLE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.VISIBLE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.GONE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.GONE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.GONE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.GONE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.GONE
            }
            4 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.VISIBLE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.VISIBLE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.VISIBLE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.GONE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.GONE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.GONE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.GONE
            }
            5 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.VISIBLE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.VISIBLE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.VISIBLE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.VISIBLE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.GONE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.GONE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.GONE
            }
            6 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.VISIBLE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.VISIBLE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.VISIBLE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.VISIBLE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.VISIBLE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.GONE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.GONE
            }
            7 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.VISIBLE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.VISIBLE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.VISIBLE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.VISIBLE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.VISIBLE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.VISIBLE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.GONE
            }
            8 -> {
                if (imageAndSummaryContainer1 != null)
                    imageAndSummaryContainer1.visibility = View.VISIBLE
                if (imageAndSummaryContainer2 != null)
                    imageAndSummaryContainer2.visibility = View.VISIBLE
                if (imageAndSummaryContainer3 != null)
                    imageAndSummaryContainer3.visibility = View.VISIBLE
                if (imageAndSummaryContainer4 != null)
                    imageAndSummaryContainer4.visibility = View.VISIBLE
                if (imageAndSummaryContainer5 != null)
                    imageAndSummaryContainer5.visibility = View.VISIBLE
                if (imageAndSummaryContainer6 != null)
                    imageAndSummaryContainer6.visibility = View.VISIBLE
                if (imageAndSummaryContainer7 != null)
                    imageAndSummaryContainer7.visibility = View.VISIBLE
                if (imageAndSummaryContainer8 != null)
                    imageAndSummaryContainer8.visibility = View.VISIBLE
            }
        }
    }

    fun setupImageContainer(callback: OnThumbnailClickedCallback, activity: Activity,
                            holder: FilesListViewViewHolder, image: ImageView, webmImage: ImageView,
                            summary: TextView, file: Files, viewModeIsDialog: Boolean,
                            reloadImages: Boolean) {
        val path: String = file.getPath()!!

        if (Formats.VIDEO_FORMATS.contains(TextUtils.getSubstringAfterDot(path))) {
            webmImage.visibility = View.VISIBLE
            setCorrectVideoImageSize(activity, webmImage, viewModeIsDialog)
        } else {
            webmImage.visibility = View.GONE
        }

        setCorrectImageSize(activity, image, file, viewModeIsDialog)
        summary.text = TextUtils.getSummaryString(activity, file)
        if (reloadImages) loadImageThumbnail(activity, image, file)
        image.setOnClickListener({
            callback.onThumbnailClicked(file)
        })
    }

    fun setCorrectImageSize(activity: Activity, image: ImageView, file: Files, viewModeIsDialog: Boolean) {
        val width: Int = UiUtils.convertDpToPixel(
                ImageManager.computeImageWidthInDp(activity, viewModeIsDialog)).toInt()
        val minHeight: Int = UiUtils.convertDpToPixel(
                ImageManager.getPreferredMinimumImageHeightInDp(activity)).toInt()
        val maxHeight: Int = UiUtils.convertDpToPixel(
                ImageManager.getPreferredMaximumImageHeightInDp(activity)).toInt()

        val widthInt = Integer.parseInt(file.getWidth())
        val heightInt = Integer.parseInt(file.getHeight())
        val aspectRatio = widthInt.toFloat() / heightInt.toFloat()
        val finalHeight = Math.round(width / aspectRatio)

        image.layoutParams.width = width

        if (finalHeight < minHeight || finalHeight > maxHeight) {
            if (finalHeight < minHeight) image.layoutParams.height = minHeight
            if (finalHeight > maxHeight) image.layoutParams.height = maxHeight
        } else image.layoutParams.height = finalHeight
        image.minimumHeight = minHeight
        image.maxHeight = maxHeight
        image.requestLayout()
    }

    fun computeImageHeightInPx(activity: Activity, file: Files, viewModeIsDialog: Boolean): Int {
        val width: Int = UiUtils.convertDpToPixel(
                ImageManager.computeImageWidthInDp(activity, viewModeIsDialog)).toInt()

        val widthInt = Integer.parseInt(file.getWidth())
        val heightInt = Integer.parseInt(file.getHeight())
        val aspectRatio = widthInt.toFloat() / heightInt.toFloat()
        val finalHeight = Math.round(width / aspectRatio)

        return finalHeight
    }

    fun setCorrectVideoImageSize(activity: Activity, image: ImageView, viewModeIsDialog: Boolean) {
        val imageWidth = UiUtils.convertDpToPixel(
                ImageManager.computeImageWidthInDp(activity, viewModeIsDialog)).toInt()
        val size = imageWidth / 2
        image.layoutParams.width = size
        image.layoutParams.height = size
    }

    fun loadImageThumbnail(activity: Activity, image: ImageView, file: Files) {
        image.setImageBitmap(null)
        if (image.animation != null) image.animation.cancel()
        image.setBackgroundColor(activity.resources.getColor(R.color.colorBackgroundDark))

        Glide.with(activity).load(Uri.parse(Dvach.DVACH_BASE_URL + file.getThumbnail()))
                .crossFade(200).placeholder(image.drawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image)
    }
}