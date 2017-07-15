package com.koresuniku.wishmaster.ui.thread_list

import android.net.Uri
import android.os.Handler
import android.text.Html
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.Dvach
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.http.thread_list_api.model.Thread
import com.koresuniku.wishmaster.system.PreferenceUtils
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.controller.ImageManager
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.view.INotifyableItemImageSizeChangedView
import com.koresuniku.wishmaster.ui.view.INotifyableLisViewAdapter
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView
import com.koresuniku.wishmaster.util.Formats

class ThreadListListViewAdapter(val mView: ThreadListListViewView) : BaseAdapter(),
        INotifyableLisViewAdapter, INotifyableItemImageSizeChangedView {
    val LOG_TAG: String = ThreadListListViewAdapter::class.java.simpleName

    val ITEM_NO_IMAGES: Int = 0
    val ITEM_SINGLE_IMAGE: Int = 1
    val ITEM_MULTIPLE_IMAGES: Int = 2

    val mHandler = Handler()
    var holdersCounter = 0
    val holders: ArrayList<ViewHolder> = ArrayList()

    override fun iNotifyDataSetChanged() {
        this.notifyDataSetChanged()
    }

    inner class ViewHolder {
        var mItemContainer: FrameLayout? = null
        var mSubjectTextView: TextView? = null
        var mCommentTextView: NoScrollTextView? = null
        var mPostsAndFilesInfo: TextView? = null
        var imageAndSummaryContainer1: RelativeLayout? = null
        var imageAndSummaryContainer2: RelativeLayout? = null
        var imageAndSummaryContainer3: RelativeLayout? = null
        var imageAndSummaryContainer4: RelativeLayout? = null
        var imageAndSummaryContainer5: RelativeLayout? = null
        var imageAndSummaryContainer6: RelativeLayout? = null
        var imageAndSummaryContainer7: RelativeLayout? = null
        var imageAndSummaryContainer8: RelativeLayout? = null
        var image: ImageView? = null
        var image1: ImageView? = null
        var image2: ImageView? = null
        var image3: ImageView? = null
        var image4: ImageView? = null
        var image5: ImageView? = null
        var image6: ImageView? = null
        var image7: ImageView? = null
        var image8: ImageView? = null
        var webmImageView: ImageView? = null
        var webmImageView1: ImageView? = null
        var webmImageView2: ImageView? = null
        var webmImageView3: ImageView? = null
        var webmImageView4: ImageView? = null
        var webmImageView5: ImageView? = null
        var webmImageView6: ImageView? = null
        var webmImageView7: ImageView? = null
        var webmImageView8: ImageView? = null
        var summary: TextView? = null
        var summary1: TextView? = null
        var summary2: TextView? = null
        var summary3: TextView? = null
        var summary4: TextView? = null
        var summary5: TextView? = null
        var summary6: TextView? = null
        var summary7: TextView? = null
        var summary8: TextView? = null

        var viewType: Int? = null
        var code: Int = -1
        var files: List<Files> = ArrayList()
    }

    override fun getCount(): Int {
        return mView.getSchema().getThreads().size
    }

    override fun getItem(position: Int): Any? {
        return mView.getSchema().getThreads()[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val size = mView.getSchema().getThreads()[position].getFiles().size
        if (size == 0) return ITEM_NO_IMAGES
        if (size == 1) return ITEM_SINGLE_IMAGE
        if (size > 1) return ITEM_MULTIPLE_IMAGES
        return ITEM_NO_IMAGES
    }

    fun inflateCorrectConvertView(position: Int, parent: ViewGroup): View {
        when (getItemViewType(position)) {
            ITEM_NO_IMAGES -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.thread_item_no_images, parent, false)
            }
            ITEM_SINGLE_IMAGE -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.thread_item_single_image, parent, false)
            }
            ITEM_MULTIPLE_IMAGES -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.thread_item_multiple_images, parent, false)
            }
        }
        return View(mView.getActivity())
    }

    fun getViewHolderInstance(itemView: View, viewType: Int): ViewHolder {
        val holder: ViewHolder = ViewHolder()
        holder.viewType = viewType

        holder.mItemContainer = itemView.findViewById(R.id.thread_item_container_container) as FrameLayout
        holder.mSubjectTextView = itemView.findViewById(R.id.thread_subject) as TextView
        holder.mCommentTextView = itemView.findViewById(R.id.thread_comment) as NoScrollTextView
        holder.mPostsAndFilesInfo = itemView.findViewById(R.id.posts_and_files_info) as TextView
        if (viewType == ITEM_SINGLE_IMAGE) {
            holder.image = itemView.findViewById(R.id.thread_image) as ImageView
            holder.webmImageView = itemView.findViewById(R.id.webm_imageview) as ImageView
            holder.summary = itemView.findViewById(R.id.image_summary) as TextView
        }
        if (viewType == ITEM_MULTIPLE_IMAGES) {
            holder.imageAndSummaryContainer1 = itemView.findViewById(R.id.image_with_summary_container_1) as RelativeLayout
            holder.imageAndSummaryContainer2 = itemView.findViewById(R.id.image_with_summary_container_2) as RelativeLayout
            holder.imageAndSummaryContainer3 = itemView.findViewById(R.id.image_with_summary_container_3) as RelativeLayout
            holder.imageAndSummaryContainer4 = itemView.findViewById(R.id.image_with_summary_container_4) as RelativeLayout
            holder.imageAndSummaryContainer5 = itemView.findViewById(R.id.image_with_summary_container_5) as RelativeLayout
            holder.imageAndSummaryContainer6 = itemView.findViewById(R.id.image_with_summary_container_6) as RelativeLayout
            holder.imageAndSummaryContainer7 = itemView.findViewById(R.id.image_with_summary_container_7) as RelativeLayout
            holder.imageAndSummaryContainer8 = itemView.findViewById(R.id.image_with_summary_container_8) as RelativeLayout
            holder.image1 = itemView.findViewById(R.id.thread_image_1) as ImageView
            holder.webmImageView1 = itemView.findViewById(R.id.webm_imageview_1) as ImageView
            holder.image2 = itemView.findViewById(R.id.thread_image_2) as ImageView
            holder.webmImageView2 = itemView.findViewById(R.id.webm_imageview_2) as ImageView
            holder.image3 = itemView.findViewById(R.id.thread_image_3) as ImageView
            holder.webmImageView3 = itemView.findViewById(R.id.webm_imageview_3) as ImageView
            holder.image4 = itemView.findViewById(R.id.thread_image_4) as ImageView
            holder.webmImageView4 = itemView.findViewById(R.id.webm_imageview_4) as ImageView
            holder.image5 = itemView.findViewById(R.id.thread_image_5) as ImageView
            holder.webmImageView5 = itemView.findViewById(R.id.webm_imageview_5) as ImageView
            holder.image6 = itemView.findViewById(R.id.thread_image_6) as ImageView
            holder.webmImageView6 = itemView.findViewById(R.id.webm_imageview_6) as ImageView
            holder.image7 = itemView.findViewById(R.id.thread_image_7) as ImageView
            holder.webmImageView7 = itemView.findViewById(R.id.webm_imageview_7) as ImageView
            holder.image8 = itemView.findViewById(R.id.thread_image_8) as ImageView
            holder.webmImageView8 = itemView.findViewById(R.id.webm_imageview_8) as ImageView
            holder.summary1 = itemView.findViewById(R.id.image_summary_1) as TextView
            holder.summary2 = itemView.findViewById(R.id.image_summary_2) as TextView
            holder.summary3 = itemView.findViewById(R.id.image_summary_3) as TextView
            holder.summary4 = itemView.findViewById(R.id.image_summary_4) as TextView
            holder.summary5 = itemView.findViewById(R.id.image_summary_5) as TextView
            holder.summary6 = itemView.findViewById(R.id.image_summary_6) as TextView
            holder.summary7 = itemView.findViewById(R.id.image_summary_7) as TextView
            holder.summary8 = itemView.findViewById(R.id.image_summary_8) as TextView
        }

        return holder
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder
        val thread: Thread = mView.getSchema().getThreads()[position]
        val files: List<Files> = thread.getFiles()

        if (convertView == null) {
            convertView = inflateCorrectConvertView(position, parent)
            holder = getViewHolderInstance(convertView, getItemViewType(position))
            holder.code = holdersCounter++
            holder.files = files
            holders.add(holder)
            convertView.tag = holders[holders.size - 1]
        } else {
            holder = convertView.tag as ViewHolder
            if (holder.viewType != getItemViewType(position)) {
                convertView = inflateCorrectConvertView(position, parent)
                Log.d(LOG_TAG, "reusing holder: " + holder.code)
                val code = holder.code
                holders.filter { it.code == code }.forEach { holders.removeAt(holders.indexOf(it)) }
                holder = getViewHolderInstance(convertView, getItemViewType(position))
                holder.code = code
                holder.files = files
                holders.add(holder)
                convertView.tag = holders[holders.size - 1]
            }
        }

        Log.d(LOG_TAG, "holders.size: " + holders.size)

        holder.mItemContainer!!.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                mView.showPostDialog(position)
                return false
            }
        })

        if (Dvach.disableSubject.contains(mView.getBoardId())) {
            holder.mSubjectTextView!!.visibility = View.GONE
        } else {
            holder.mSubjectTextView!!.text = Html.fromHtml(thread.getSubject())
        }

        setupImages(holder, true)

        val maxLines = PreferenceUtils.getSharedPreferences(mView.getActivity()).getString(
                mView.getActivity().getString(R.string.pref_lines_count_key),
                mView.getActivity().getString(R.string.pref_lines_count_default)).toInt()
        if (maxLines == 0) holder.mCommentTextView!!.maxLines = Int.MAX_VALUE
        else holder.mCommentTextView!!.maxLines = maxLines

        holder.mCommentTextView!!.text = Html.fromHtml(thread.getComment())
        holder.mPostsAndFilesInfo!!.text = TextUtils.getPostsAndFilesString(
                thread.getPostsCount().toInt(), thread.getFilesCount().toInt())

        return convertView
    }

    fun getViewForDialog(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder
        val thread: Thread = mView.getSchema().getThreads()[position]
        val files: List<Files> = thread.getFiles()

        if (convertView == null) {
            convertView = inflateCorrectConvertView(position, parent)
            holder = getViewHolderInstance(convertView, getItemViewType(position))
            holder.code = holdersCounter++
            holder.files = files
            holders.add(holder)
            convertView.tag = holders[holders.size - 1]
        } else {
            holder = convertView.tag as ViewHolder
            if (holder.viewType != getItemViewType(position)) {
                convertView = inflateCorrectConvertView(position, parent)
                Log.d(LOG_TAG, "reusing holder: " + holder.code)
                val code = holder.code
                holders.filter { it.code == code }.forEach { holders.removeAt(holders.indexOf(it)) }
                holder = getViewHolderInstance(convertView, getItemViewType(position))
                holder.code = code
                holder.files = files
                holders.add(holder)
                convertView.tag = holders[holders.size - 1]
            }
        }

        Log.d(LOG_TAG, "holders.size: " + holders.size)

        holder.mItemContainer!!.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                mView.showPostDialog(position)
                return false
            }
        })


        if (Dvach.disableSubject.contains(mView.getBoardId())) {
            holder.mSubjectTextView!!.visibility = View.GONE
        } else {
            holder.mSubjectTextView!!.text = Html.fromHtml(thread.getSubject())
        }

        setupImages(holder, true)

        holder.mCommentTextView!!.maxLines = Int.MAX_VALUE

        holder.mCommentTextView!!.text = Html.fromHtml(thread.getComment())
        holder.mPostsAndFilesInfo!!.text = TextUtils.getPostsAndFilesString(
                thread.getPostsCount().toInt(), thread.getFilesCount().toInt())

        return convertView
    }

    fun setupImages(holder: ViewHolder, reloadImages: Boolean) {
        val filesSize = holder.files.size
        switchImagesVisibility(holder, filesSize)

        if (filesSize != 0) {
            for (file in holder.files) {
                if (filesSize == 1) {
                    setupImageContainer(holder.image!!, holder.webmImageView!!,
                            holder.summary!!, file, true, reloadImages)
                }
                if (filesSize > 1) {
                    when (holder.files.indexOf(file)) {
                        0 -> setupImageContainer(holder.image1!!, holder.webmImageView1!!,
                                holder.summary1!!, file, true, reloadImages)
                        1 -> setupImageContainer(holder.image2!!, holder.webmImageView2!!,
                                holder.summary2!!, file, true, reloadImages)
                        2 -> setupImageContainer(holder.image3!!, holder.webmImageView3!!,
                                holder.summary3!!, file, true, reloadImages)
                        3 -> setupImageContainer(holder.image4!!, holder.webmImageView4!!,
                                holder.summary4!!, file, true, reloadImages)
                        4 -> setupImageContainer(holder.image5!!, holder.webmImageView5!!,
                                holder.summary5!!, file, true, reloadImages)
                        5 -> setupImageContainer(holder.image6!!, holder.webmImageView6!!,
                                holder.summary6!!, file, true, reloadImages)
                        6 -> setupImageContainer(holder.image7!!, holder.webmImageView7!!,
                                holder.summary7!!, file, true, reloadImages)
                        7 -> setupImageContainer(holder.image8!!, holder.webmImageView8!!,
                                holder.summary8!!, file, true, reloadImages)
                    }
                }
            }
        }
    }


    private fun switchImagesVisibility(holder: ViewHolder, filesSize: Int) {
        switchImagesVisibility(
                holder.imageAndSummaryContainer1, holder.imageAndSummaryContainer2,
                holder.imageAndSummaryContainer3, holder.imageAndSummaryContainer4,
                holder.imageAndSummaryContainer5, holder.imageAndSummaryContainer6,
                holder.imageAndSummaryContainer7, holder.imageAndSummaryContainer8,
                filesSize)
    }

    private fun switchImagesVisibility(
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


    fun setupImageContainer(image: ImageView, webmImage: ImageView, summary: TextView,
                            file: Files, viewModeIsDialog: Boolean, reloadImages: Boolean) {
        val path: String = file.getPath()

        if (path.substring(path.length - 4, path.length) == Formats.WEBM_FORMAT) {
            webmImage.visibility = View.VISIBLE
            setCorrectVideoImageSize(webmImage, viewModeIsDialog)
        } else {
            webmImage.visibility = View.GONE
        }

        setCorrectImageSize(image, file, viewModeIsDialog)
        summary.text = TextUtils.getSummaryString(mView.getActivity(), file)
        if (reloadImages) loadImageThumbnail(image, file)
    }

    fun setCorrectImageSize(image: ImageView, file: Files, viewModeIsDialog: Boolean) {
        val width: Int = UIUtils.convertDpToPixel(
                ImageManager.computeImageWidthInDp(mView.getActivity(), viewModeIsDialog)).toInt()
        val minHeight: Int = UIUtils.convertDpToPixel(
                ImageManager.getPreferredMinimumImageHeightInDp(mView.getActivity())).toInt()
        val maxHeight: Int = UIUtils.convertDpToPixel(
                ImageManager.getPreferredMaximumImageHeightInDp(mView.getActivity())).toInt()

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

    fun setCorrectVideoImageSize(image: ImageView, viewModeIsDialog: Boolean) {
        val imageWidth = UIUtils.convertDpToPixel(
                ImageManager.computeImageWidthInDp(mView.getActivity(), viewModeIsDialog)).toInt()
        val size = imageWidth / 2
        image.layoutParams.width = size
        image.layoutParams.height = size
    }

    fun loadImageThumbnail(image: ImageView, file: Files) {
        image.setImageBitmap(null)
        if (image.animation != null) image.animation.cancel()
        image.setBackgroundColor(mView.getActivity().resources.getColor(R.color.colorBackgroundDark))

        Glide.with(mView.getActivity()).load(Uri.parse(Dvach.DVACH_BASE_URL + file.getThumbnail()))
                .crossFade(200).placeholder(image.drawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(image)
    }

    fun notifyItemCommentTextViewChanged() {
        val value = PreferenceUtils.getSharedPreferences(mView.getActivity()).getString(
                mView.getActivity().getString(R.string.pref_lines_count_key),
                mView.getActivity().getString(R.string.pref_lines_count_default)).toInt()
        holders.forEach {
            if (value == 0) it.mCommentTextView!!.maxLines = Int.MAX_VALUE
            else it.mCommentTextView!!.maxLines = value
        }
    }

    override fun notifyItemImageSizeChanged() {
        holders.forEach { setupImages(it, false) }
    }
}
