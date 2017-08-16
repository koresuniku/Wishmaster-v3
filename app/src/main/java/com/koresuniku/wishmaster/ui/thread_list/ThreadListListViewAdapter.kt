package com.koresuniku.wishmaster.ui.thread_list

import android.app.Activity
import android.content.res.Configuration
import android.support.v4.view.ViewPager
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
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.controller.ClickableAdapter
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.INotifyableItemImageSizeChangedView
import com.koresuniku.wishmaster.ui.controller.view_interface.INotifyableListViewAdapter
import com.koresuniku.wishmaster.ui.gallery.*
import com.koresuniku.wishmaster.ui.text.comment_link_movement_method.CommentLinkMovementMethod
import com.koresuniku.wishmaster.ui.text.SpanTagHandlerCompat
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView
import com.pixplicity.htmlcompat.HtmlCompat
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find
import org.jetbrains.anko.topPadding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ThreadListListViewAdapter(val mView: ThreadListListViewView) : BaseAdapter(),
        INotifyableListViewAdapter, INotifyableItemImageSizeChangedView, ActionBarView,
        ClickableAdapter, ListViewAdapterUtils.OnThumbnailClickedCallback {
    val LOG_TAG: String = ThreadListListViewAdapter::class.java.simpleName

    val ITEM_NO_IMAGES: Int = 0
    val ITEM_SINGLE_IMAGE: Int = 1
    val ITEM_MULTIPLE_IMAGES: Int = 2

    var holdersCounter = 0
    val holders: ArrayList<ViewHolder> = ArrayList()

    val mGalleryPresenter = GalleryPresenter(this)

//    val mGalleryActionBarUnit: GalleryActionBarUnit = GalleryActionBarUnit(this)
//    var mGalleryPager: ViewPager? = null
//    var mGalleryPagerAdapter: GalleryPagerAdapter? = null
//    var mGalleryOnPageChangeListener: GalleryOnPageChangeListener? = null

    override fun iNotifyDataSetChanged() {
        this.notifyDataSetChanged()
    }

    override fun onThumbnailClicked(file: Files) {

    }

    inner class ViewHolder : FilesListViewViewHolder() {
        var mItemContainer: RelativeLayout? = null
        var mSubjectTextView: TextView? = null
        var mCommentTextView: NoScrollTextView? = null
        var mPostsAndFilesInfo: TextView? = null

//        override fun showImageOrVideo(file: Files) {
//            mView.notifyGalleryShown()
//
//            UIVisibilityManager.setBarsTranslucent(mView.getActivity(), true)
//            mView.getGalleryLayoutContainer().visibility = View.VISIBLE
//            mGalleryActionBarUnit.setupTitleAndSubtitle(file, files!!.indexOf(file), files!!.count())
//
//            mGalleryPager = mView.getViewPager()
//            mGalleryPagerAdapter = GalleryPagerAdapter(
//                    mView.getAppCompatActivity().supportFragmentManager, this,
//                    files!!, files!!.indexOf(file))
//            mGalleryPager!!.adapter = mGalleryPagerAdapter
//            mGalleryPager!!.offscreenPageLimit = 1
//            mGalleryPager!!.currentItem = files!!.indexOf(file)
//            mGalleryOnPageChangeListener = GalleryOnPageChangeListener(this)
//            mGalleryPager!!.addOnPageChangeListener(mGalleryOnPageChangeListener)
//        }
//
//        override fun getGalleryActionBar(): GalleryActionBarUnit {
//            return mGalleryActionBarUnit
//        }
//
//        override fun onGalleryHidden() {
//            mView.notifyGalleryHidden()
//        }
//
//        override fun getViewPager(): ViewPager {
//            return mGalleryPager!!
//        }
//
//        override fun getActivity(): Activity {
//            return mView.getActivity()
//        }
//
//        override fun onPageChanged(newPosition: Int) {
//            mGalleryPagerAdapter!!.onPageChanged(newPosition)
//            val file = files!![newPosition]
//            mGalleryActionBarUnit.onPageChanged(file, newPosition, files!!.count())
//        }

        init {
            files = ArrayList()
        }

    }

    override fun onClick(threadNumber: String) {
        mView.openThread(threadNumber)
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
//

    }

    override fun onBackPressedOverridden(): Boolean {



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

    override fun onLongClick(threadNumber: String) {
        for (thread in mView.getSchema().getThreads()) {
            if (thread.getNum() == threadNumber) {
                mView.showPostDialog(mView.getSchema().getThreads().indexOf(thread))
                break
            }
        }
    }

    override fun getViewTypeCount(): Int {
        return 3
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder
        val thread: Thread = mView.getSchema().getThreads()[position]

        if (convertView == null) {
            Log.d(LOG_TAG, "------------------")
            Log.d(LOG_TAG, "inflating new view")
            convertView = inflateCorrectConvertView(position, parent)
            holder = getViewHolderInstance(convertView, getItemViewType(position))
            convertView.tag = holder
            holders.add(holder)
        } else {
            Log.d(LOG_TAG, "------------")
            Log.d(LOG_TAG, "reusing view")
            holder = convertView.tag as ViewHolder
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
        ListViewAdapterUtils.setupImages(this, mView.getActivity(), holder, false, true)

        val maxLines = PreferenceUtils.getSharedPreferences(mView.getActivity()).getString(
                mView.getActivity().getString(R.string.pref_lines_count_key),
                mView.getActivity().getString(R.string.pref_lines_count_default)).toInt()
        if (maxLines == 0) holder.mCommentTextView!!.maxLines = Int.MAX_VALUE
        else holder.mCommentTextView!!.maxLines = maxLines

        val commentDocument: Document = Jsoup.parse(thread.getComment())
        val commentElements: Elements = commentDocument.select(SpanTagHandlerCompat.SPAN_TAG)

        commentElements.forEach { it.getElementsByAttributeValue(
                SpanTagHandlerCompat.CLASS_ATTR, SpanTagHandlerCompat.QUOTE_VALUE)
                .tagName(SpanTagHandlerCompat.QUOTE_TAG)
        }
        commentElements.forEach {
            it.getElementsByAttributeValue(
                    SpanTagHandlerCompat.CLASS_ATTR, SpanTagHandlerCompat.SPOILER_VALUE)
                    .tagName(SpanTagHandlerCompat.SPOILER_TAG)
        }

        holder.mCommentTextView!!.text = HtmlCompat.fromHtml(
                mView.getActivity(), commentDocument.html(), 0,
                null, SpanTagHandlerCompat(mView.getActivity()))
        holder.mCommentTextView!!.linksClickable = false
        holder.mCommentTextView!!.movementMethod =
                CommentLinkMovementMethod(mView.getActivity(), this, thread.getNum())
        holder.mPostsAndFilesInfo!!.text = TextUtils.getPostsAndFilesString(
                thread.getPostsCount().toInt(), thread.getFilesCount().toInt())

        return convertView
    }

    fun getViewForDialog(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder  = ViewHolder()
        val thread: Thread = mView.getSchema().getThreads()[position]

        if (convertView == null) {
            convertView = inflateCorrectConvertView(position, parent)
            holder = getViewHolderInstance(convertView, getItemViewType(position))
            convertView.tag = holder
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

        ListViewAdapterUtils.setupImages(this, mView.getActivity(), holder, true, true)
        val commentDocument: Document = Jsoup.parse(thread.getComment())
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

        holder.mCommentTextView!!.text = HtmlCompat.fromHtml(
                mView.getActivity(), commentDocument.html(), 0,
                null, SpanTagHandlerCompat(mView.getActivity()))
        holder.mCommentTextView!!.maxLines = Int.MAX_VALUE
        holder.mCommentTextView!!.linksClickable = false
        holder.mCommentTextView!!.movementMethod =
                CommentLinkMovementMethod(mView.getActivity(), this, thread.getNum())
        holder.mPostsAndFilesInfo!!.text = TextUtils.getPostsAndFilesString(
                thread.getPostsCount().toInt(), thread.getFilesCount().toInt())

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
        holders.forEach { ListViewAdapterUtils.setupImages(this, mView.getActivity(), it, false, false) }
    }
}
