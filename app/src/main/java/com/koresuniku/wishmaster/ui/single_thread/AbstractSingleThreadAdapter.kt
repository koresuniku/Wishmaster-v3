package com.koresuniku.wishmaster.ui.single_thread

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.http.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.controller.view_interface.AppCompatActivityView
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class AbstractSingleThreadAdapter(val mView: SingleThreadListViewView,
                                           val mSchema: BaseJsonSchemaImpl) :
        BaseAdapter(), ActionBarView, AppCompatActivityView,
        ListViewAdapterUtils.OnThumbnailClickedCallback {

    val holders: ArrayList<ViewHolderAndFiles> = ArrayList()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event.anEvent) {
            LifecycleEvent.onStart -> EventBus.getDefault().register(this)
            LifecycleEvent.onStop -> EventBus.getDefault().unregister(this)
        }
    }


    inner class ViewHolderAndFiles : CommentAndFilesListViewViewHolder() {
        var mItemContainer: RelativeLayout? = null
        var mNumberAndTimeInfo: TextView? = null
        var mAnswers: TextView? = null
        var postNumber: String? = null

        override fun getActivity(): Activity = mView.getActivity()

        init {
            files = ArrayList()
        }
    }

    private fun getFilesList(): List<Files> {
        val filesList: ArrayList<Files> = ArrayList()
        for (post in mSchema.getPosts()!!) {
            filesList += post.getFiles()
        }
        return filesList
    }

    fun inflateCorrectConvertView(position: Int, parent: ViewGroup): View {
        when (getItemViewType(position)) {
            ListViewAdapterUtils.ITEM_NO_IMAGES -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.post_item_no_images, parent, false)
            }
            ListViewAdapterUtils.ITEM_SINGLE_IMAGE -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.post_item_single_image, parent, false)
            }
            ListViewAdapterUtils.ITEM_MULTIPLE_IMAGES -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.post_item_multiple_images, parent, false)
            }
        }
        return View(mView.getActivity())
    }

    fun getViewHolderInstance(itemView: View, viewType: Int): ViewHolderAndFiles {
        val holder: ViewHolderAndFiles = ViewHolderAndFiles()
        holder.viewType = viewType

        holder.mItemContainer = itemView.findViewById(R.id.post_item_container) as RelativeLayout
        holder.mNumberAndTimeInfo = itemView.findViewById(R.id.post_number_and_time_info) as TextView
        holder.mCommentTextView = itemView.findViewById(R.id.post_comment) as NoScrollTextView
        holder.mAnswers = itemView.findViewById(R.id.answers) as TextView
        if (viewType == ListViewAdapterUtils.ITEM_SINGLE_IMAGE) {
            holder.imageAndSummaryContainer = itemView.findViewById(R.id.image_and_summary_container) as RelativeLayout
            holder.image = itemView.findViewById(R.id.post_image) as ImageView
            holder.webmImageView = itemView.findViewById(R.id.webm_imageview) as ImageView
            holder.summary = itemView.findViewById(R.id.image_summary) as TextView
        }
        if (viewType == ListViewAdapterUtils.ITEM_MULTIPLE_IMAGES) {
            holder.imageAndSummaryContainer1 = itemView.findViewById(R.id.image_with_summary_container_1) as RelativeLayout
            holder.imageAndSummaryContainer2 = itemView.findViewById(R.id.image_with_summary_container_2) as RelativeLayout
            holder.imageAndSummaryContainer3 = itemView.findViewById(R.id.image_with_summary_container_3) as RelativeLayout
            holder.imageAndSummaryContainer4 = itemView.findViewById(R.id.image_with_summary_container_4) as RelativeLayout
            holder.imageAndSummaryContainer5 = itemView.findViewById(R.id.image_with_summary_container_5) as RelativeLayout
            holder.imageAndSummaryContainer6 = itemView.findViewById(R.id.image_with_summary_container_6) as RelativeLayout
            holder.imageAndSummaryContainer7 = itemView.findViewById(R.id.image_with_summary_container_7) as RelativeLayout
            holder.imageAndSummaryContainer8 = itemView.findViewById(R.id.image_with_summary_container_8) as RelativeLayout
            holder.image1 = itemView.findViewById(R.id.post_image_1) as ImageView
            holder.webmImageView1 = itemView.findViewById(R.id.webm_imageview_1) as ImageView
            holder.image2 = itemView.findViewById(R.id.post_image_2) as ImageView
            holder.webmImageView2 = itemView.findViewById(R.id.webm_imageview_2) as ImageView
            holder.image3 = itemView.findViewById(R.id.post_image_3) as ImageView
            holder.webmImageView3 = itemView.findViewById(R.id.webm_imageview_3) as ImageView
            holder.image4 = itemView.findViewById(R.id.post_image_4) as ImageView
            holder.webmImageView4 = itemView.findViewById(R.id.webm_imageview_4) as ImageView
            holder.image5 = itemView.findViewById(R.id.post_image_5) as ImageView
            holder.webmImageView5 = itemView.findViewById(R.id.webm_imageview_5) as ImageView
            holder.image6 = itemView.findViewById(R.id.post_image_6) as ImageView
            holder.webmImageView6 = itemView.findViewById(R.id.webm_imageview_6) as ImageView
            holder.image7 = itemView.findViewById(R.id.post_image_7) as ImageView
            holder.webmImageView7 = itemView.findViewById(R.id.webm_imageview_7) as ImageView
            holder.image8 = itemView.findViewById(R.id.post_image_8) as ImageView
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

    open fun getPost(position: Int): Post = mSchema.getPosts()!![position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val holder: ViewHolderAndFiles
        val post: Post = getPost(position)

        if (convertView == null) {
            //Log.d(LOG_TAG, "convertview is null, inflating new viewholder")
            convertView = inflateCorrectConvertView(position, parent!!)
            holder = getViewHolderInstance(convertView, getItemViewType(position))
            holder.postNumber = post.getNum()
            convertView.tag = holder
            holders.add(holder)
        } else {
            holder = convertView.tag as ViewHolderAndFiles
            holder.postNumber = post.getNum()
        }

        holder.files = post.getFiles()
        ListViewAdapterUtils.setupImages(this, mView.getActivity(), holder, false, true)

        holder.mItemContainer!!.setOnLongClickListener { mView.showPostDialog(position); false }

        val mNumberAndTimeSpannable =
                TextUtils.getNumberAndTimeInfoSpannableString(mView.getActivity(), position, post)
        holder.mNumberAndTimeInfo!!.setText(mNumberAndTimeSpannable, TextView.BufferType.SPANNABLE)

        //ListViewAdapterUtils.setupComment(holder, post, mAnswersManager)

        holder.postNumber = post.getNum()
        setupAnswers(holder, post)

        holder.mAnswers!!.bringToFront()

        return convertView
    }

    abstract fun setupAnswers(holder: CommentAndFilesListViewViewHolder, post: Post)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val size = mSchema.getPosts()!![position].getFiles().size
        if (size == 0) return ListViewAdapterUtils.ITEM_NO_IMAGES
        if (size == 1) return ListViewAdapterUtils.ITEM_SINGLE_IMAGE
        if (size > 1) return ListViewAdapterUtils.ITEM_MULTIPLE_IMAGES
        return ListViewAdapterUtils.ITEM_NO_IMAGES
    }

    override fun getCount(): Int = mSchema.getPosts()!!.size

    override fun getToolbarContainer(): FrameLayout {
        return FrameLayout(mView.getActivity())
    }

    override fun setupActionBarTitle() {

    }

    override fun onBackPressedOverridden(): Boolean {
        return false
    }

    override fun onThumbnailClicked(file: Files) {

    }
}