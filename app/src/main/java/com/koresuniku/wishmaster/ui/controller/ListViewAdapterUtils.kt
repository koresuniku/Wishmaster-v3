package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.Dvach
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.thread_list.ThreadListListViewAdapter
import com.koresuniku.wishmaster.util.Formats

object ListViewAdapterUtils {

    fun setupImages(activity: Activity, holder: FilesListViewViewHolder, viewModeIsDialog: Boolean, reloadImages: Boolean) {
        val filesSize = holder.files!!.size
        switchImagesVisibility(holder, filesSize)

        if (filesSize != 0) {
            for (file in holder.files!!) {
                if (filesSize == 1) {
                    setupImageContainer(activity, holder.image!!, holder.webmImageView!!,
                            holder.summary!!, file, viewModeIsDialog, reloadImages)
                }
                if (filesSize > 1) {
                    when (holder.files!!.indexOf(file)) {
                        0 -> setupImageContainer(activity, holder.image1!!, holder.webmImageView1!!,
                                holder.summary1!!, file, viewModeIsDialog, reloadImages)
                        1 -> setupImageContainer(activity, holder.image2!!, holder.webmImageView2!!,
                                holder.summary2!!, file, viewModeIsDialog, reloadImages)
                        2 -> setupImageContainer(activity, holder.image3!!, holder.webmImageView3!!,
                                holder.summary3!!, file, viewModeIsDialog, reloadImages)
                        3 -> setupImageContainer(activity, holder.image4!!, holder.webmImageView4!!,
                                holder.summary4!!, file, viewModeIsDialog, reloadImages)
                        4 -> setupImageContainer(activity, holder.image5!!, holder.webmImageView5!!,
                                holder.summary5!!, file, viewModeIsDialog, reloadImages)
                        5 -> setupImageContainer(activity, holder.image6!!, holder.webmImageView6!!,
                                holder.summary6!!, file, viewModeIsDialog, reloadImages)
                        6 -> setupImageContainer(activity, holder.image7!!, holder.webmImageView7!!,
                                holder.summary7!!, file, viewModeIsDialog, reloadImages)
                        7 -> setupImageContainer(activity, holder.image8!!, holder.webmImageView8!!,
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

    fun setupImageContainer(activity: Activity, image: ImageView, webmImage: ImageView, summary: TextView,
                            file: Files, viewModeIsDialog: Boolean, reloadImages: Boolean) {
        val path: String = file.getPath()

        if (Formats.VIDEO_FORMATS.contains(TextUtils.getSubstringAfterDot(path))) {
            webmImage.visibility = View.VISIBLE
            setCorrectVideoImageSize(activity, webmImage, viewModeIsDialog)
        } else {
            webmImage.visibility = View.GONE
        }

        setCorrectImageSize(activity, image, file, viewModeIsDialog)
        summary.text = TextUtils.getSummaryString(activity, file)
        if (reloadImages) loadImageThumbnail(activity, image, file)
    }

    fun setCorrectImageSize(activity: Activity, image: ImageView, file: Files, viewModeIsDialog: Boolean) {
        val width: Int = UIUtils.convertDpToPixel(
                ImageManager.computeImageWidthInDp(activity, viewModeIsDialog)).toInt()
        val minHeight: Int = UIUtils.convertDpToPixel(
                ImageManager.getPreferredMinimumImageHeightInDp(activity)).toInt()
        val maxHeight: Int = UIUtils.convertDpToPixel(
                ImageManager.getPreferredMaximumImageHeightInDp(activity)).toInt()

        val widthInt = Integer.parseInt(file.getWidth())
        val heightInt = Integer.parseInt(file.getHeight())
        val aspectRatio = widthInt.toFloat() / heightInt.toFloat()
        val finalHeight = Math.round(width / aspectRatio)

        image.contentDescription = finalHeight.toString()

        image.layoutParams.width = width

        if (finalHeight < minHeight || finalHeight > maxHeight) {
            if (finalHeight < minHeight) image.layoutParams.height = minHeight
            if (finalHeight > maxHeight) image.layoutParams.height = maxHeight
        } else image.layoutParams.height = finalHeight
        image.minimumHeight = minHeight
        image.maxHeight = maxHeight
        image.requestLayout()
    }

    fun setCorrectVideoImageSize(activity: Activity, image: ImageView, viewModeIsDialog: Boolean) {
        val imageWidth = UIUtils.convertDpToPixel(
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
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(image)
    }
}