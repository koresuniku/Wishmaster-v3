package com.koresuniku.wishmaster.ui.thread_list

import android.content.res.Configuration
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.Dvach
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.http.thread_list_api.model.Thread
import com.koresuniku.wishmaster.system.PreferenceUtils
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.INotifyableItemImageSizeChangedView
import com.koresuniku.wishmaster.ui.controller.view_interface.INotifyableListViewAdapter
import com.koresuniku.wishmaster.ui.gallery.GalleryActionBarUnit
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.topPadding

class ThreadListListViewAdapter(val mView: ThreadListListViewView) : BaseAdapter(),
        INotifyableListViewAdapter, INotifyableItemImageSizeChangedView, ActionBarView {
    val LOG_TAG: String = ThreadListListViewAdapter::class.java.simpleName

    val ITEM_NO_IMAGES: Int = 0
    val ITEM_SINGLE_IMAGE: Int = 1
    val ITEM_MULTIPLE_IMAGES: Int = 2

    val mHandler = Handler()
    var holdersCounter = 0
    val holders: ArrayList<ViewHolder> = ArrayList()
    val mGalleryActionBarUnit: GalleryActionBarUnit = GalleryActionBarUnit(this)

    override fun iNotifyDataSetChanged() {
        this.notifyDataSetChanged()
    }

    inner class ViewHolder : FilesListViewViewHolder() {
        var mItemContainer: RelativeLayout? = null
        var mSubjectTextView: TextView? = null
        var mCommentTextView: NoScrollTextView? = null
        var mPostsAndFilesInfo: TextView? = null

        var viewType: Int? = null
        var code: Int = -1

        override fun showImageOrVideo(file: Files) {
            UIVisibilityManager.setBarsTranslucent(mView.getActivity(), true)
            mView.getGalleryLayoutContainer().visibility = View.VISIBLE
        }

        init {
            files = ArrayList()
        }
    }

    override fun getToolbarContainer(): FrameLayout {
        return mView.getActivity().find<FrameLayout>(R.id.gallery_toolbar_container)
    }

    override fun getAppCompatActivity(): AppCompatActivity {
        return mView.getAppCompatActivity()
    }

    override fun setupActionBarTitle() {

    }

    fun onConfigurationChanged(configuration: Configuration) {
        mGalleryActionBarUnit.onConfigurationChanged(configuration)
    }

    override fun onBackPressedOverridden(): Boolean {
        if (mView.getGalleryLayoutContainer().visibility == View.VISIBLE) {
            UIVisibilityManager.setBarsTranslucent(mView.getActivity(), false)
            mView.getGalleryLayoutContainer().visibility = View.GONE
            return true
        }


        return false
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

        holder.mItemContainer = itemView.findViewById(R.id.thread_item_container) as RelativeLayout
        holder.mSubjectTextView = itemView.findViewById(R.id.post_subject) as TextView
        holder.mCommentTextView = itemView.findViewById(R.id.post_comment) as NoScrollTextView
        holder.mPostsAndFilesInfo = itemView.findViewById(R.id.answers) as TextView
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
            convertView.tag = holder
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
                convertView.tag = holder
            }
        }

        Log.d(LOG_TAG, "holders.size: " + holders.size)

        holder.mItemContainer!!.onClick { mView.openThread(thread.getNum()) }

//        holder.mItemContainer!!.setOnClickListener {object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                mView.openThread(thread.getNum())
//            }
//        }}

        holder.mItemContainer!!.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                mView.showPostDialog(position)
                return false
            }
        })

        if (Dvach.disableSubject.contains(mView.getBoardId()) || thread.getSubject().isEmpty()) {
            holder.mSubjectTextView!!.visibility = View.GONE
            holder.mItemContainer!!.topPadding =
                    mView.getActivity().dimen(R.dimen.post_item_side_padding)
        } else {
            holder.mSubjectTextView!!.visibility = View.VISIBLE
            holder.mSubjectTextView!!.text = Html.fromHtml(thread.getSubject())
            holder.mItemContainer!!.topPadding =
                    mView.getActivity().dimen(R.dimen.post_item_side_padding) / 2
        }

        holder.files = thread.getFiles()

        ListViewAdapterUtils.setupImages(mView.getActivity(), holder, false, true)

        val maxLines = PreferenceUtils.getSharedPreferences(mView.getActivity()).getString(
                mView.getActivity().getString(R.string.pref_lines_count_key),
                mView.getActivity().getString(R.string.pref_lines_count_default)).toInt()
        if (maxLines == 0) holder.mCommentTextView!!.maxLines = Int.MAX_VALUE
        else holder.mCommentTextView!!.maxLines = maxLines

        holder.mCommentTextView!!.text = Html.fromHtml(thread.getComment())
        holder.mPostsAndFilesInfo!!.text = TextUtils.getPostsAndFilesString(
                thread.getPostsCount().toInt(), thread.getFilesCount().toInt())

        holders.add(holder)
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
            convertView.tag = holder
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
                convertView.tag = holder
            }
        }

        Log.d(LOG_TAG, "holders.size: " + holders.size)

        holder.mItemContainer!!.setOnClickListener { mView.openThread(thread.getNum()) }

        holder.mItemContainer!!.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                mView.showPostDialog(position)
                return false
            }
        })


        if (Dvach.disableSubject.contains(mView.getBoardId()) || thread.getSubject().isEmpty()) {
            holder.mSubjectTextView!!.visibility = View.GONE
            holder.mItemContainer!!.topPadding =
                    mView.getActivity().dimen(R.dimen.post_item_side_padding)
        } else {
            holder.mSubjectTextView!!.visibility = View.VISIBLE
            holder.mSubjectTextView!!.text = Html.fromHtml(thread.getSubject())
            holder.mItemContainer!!.topPadding =
                    mView.getActivity().dimen(R.dimen.post_item_side_padding) / 2
        }

        holder.files = thread.getFiles()

        ListViewAdapterUtils.setupImages(mView.getActivity(), holder, true, true)

        holder.mCommentTextView!!.maxLines = Int.MAX_VALUE

        holder.mCommentTextView!!.text = Html.fromHtml(thread.getComment())
        holder.mPostsAndFilesInfo!!.text = TextUtils.getPostsAndFilesString(
                thread.getPostsCount().toInt(), thread.getFilesCount().toInt())

        holders.add(holder)
        return convertView
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
        holders.forEach { ListViewAdapterUtils.setupImages(mView.getActivity(), it, false, false) }
    }
}
