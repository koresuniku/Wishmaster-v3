package com.koresuniku.wishmaster.ui.single_thread

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.IntentUtils
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.http.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.controller.DialogManager
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.*
import com.koresuniku.wishmaster.ui.gallery.*
import com.koresuniku.wishmaster.ui.posting.PostingActivity
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersManager
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersManagerView
import com.koresuniku.wishmaster.ui.single_thread.answers.OnThumbnailClickedEvent
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find


open class SingleThreadListViewAdapter(val mView: SingleThreadListViewView,
                                       var mSchema: BaseJsonSchemaImpl,
                                       val mForDialog: Boolean) :
        BaseAdapter(), INotifyableListViewAdapter, AnswersManagerView, ActionBarView,
        DialogManager.GalleryVisibilityListener, AppCompatActivityView,
        ListViewAdapterUtils.OnThumbnailClickedCallback {
    open val LOG_TAG: String = SingleThreadListViewAdapter::class.java.simpleName


    val holders: ArrayList<ViewHolderAndFiles> = ArrayList()
    open var mAnswersManager: AnswersManager = AnswersManager(this)

    private val mGalleryPresenter = GalleryPresenter(this)

    init {
        EventBus.getDefault().register(this)
    }

    open fun initAnswersManager() {
        mAnswersManager.initAnswersMap()
        mAnswersManager.assignAnswersToPosts()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event.anEvent) {
            LifecycleEvent.onStart ->
                if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
            LifecycleEvent.onStop ->
                if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onThumbnailClickedEvent(event: OnThumbnailClickedEvent) {
        Log.d(LOG_TAG, "onThumbnailClickedEvent:")
        onThumbnailClicked(event.file)
    }

    override fun onThumbnailClicked(file: Files) {
        mGalleryPresenter.showImageOrVideo(getFilesList(), file)
    }

    override fun getSingleThreadListViewView(): SingleThreadListViewView = mView

    inner class ViewHolderAndFiles(activity: Activity) : FilesListViewViewHolder(activity) {
        var mItemContainer: RelativeLayout? = null
        var mNumberAndTimeInfo: TextView? = null
        var mAnswers: TextView? = null
        var postNumber: String? = null

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

    override fun getContext(): Context = mView.getActivity()

    override fun getActivity(): Activity = mView.getActivity()

    override fun getToolbarContainer(): FrameLayout = mView.getActivity().find(R.id.gallery_toolbar_container)

    override fun getAppCompatActivity(): AppCompatActivity = mView.getAppCompatActivity()

    override fun onGalleryShown() {}

    override fun onGalleryHidden() {}

    override fun setupActionBarTitle() {}

    override fun onBackPressedOverridden(): Boolean = mGalleryPresenter.onBackPressed()

    fun onConfigurationChanged(configuration: Configuration) {
        mGalleryPresenter.onConfigurationChanged(configuration)
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
        val holder: ViewHolderAndFiles = ViewHolderAndFiles(getActivity())
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


    override fun getViewTypeCount(): Int = 3

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
        ListViewAdapterUtils.setupImages(this, mView.getActivity(), holder, mForDialog, true)

        Log.d(LOG_TAG, "viewholder.size: ${holders.size}")

        holder.mItemContainer!!.setOnLongClickListener {
            //mView.showPostDialog(position)
            val intent = Intent(mView.getActivity(), PostingActivity::class.java)

            intent.putExtra(IntentUtils.WHOM_TO_ANSWER_CODE, post.getNum())

            if (DeviceUtils.sdkIsLollipopOrHigher()) {
                mView.getActivity().startActivity(intent)
                //this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            } else mView.getAppCompatActivity().startActivity(intent)

            false
        }

        val mNumberAndTimeSpannable = TextUtils.getNumberAndTimeInfoSpannableString(
                mView.getActivity(), post.getPostNumberAsc(), post)
        holder.mNumberAndTimeInfo!!.setText(mNumberAndTimeSpannable, TextView.BufferType.SPANNABLE)
        //Log.d(LOG_TAG, "raw html: ${post.getComment()}")
        ListViewAdapterUtils.setupComment(holder, post, mAnswersManager, mForDialog)

        holder.postNumber = post.getNum()
        setupAnswers(holder, post)

        holder.mAnswers!!.bringToFront()

        postGetView(holder)

        return convertView
    }

    open fun postGetView(holder: ViewHolderAndFiles) {

    }

    override fun getItem(position: Int): Any = mSchema.getPosts()!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = mSchema.getPosts()!!.size

    override fun getItemViewType(position: Int): Int {
        val size = mSchema.getPosts()!![position].getFiles().size
        if (size == 0) return ListViewAdapterUtils.ITEM_NO_IMAGES
        if (size == 1) return ListViewAdapterUtils.ITEM_SINGLE_IMAGE
        if (size > 1) return ListViewAdapterUtils.ITEM_MULTIPLE_IMAGES
        return ListViewAdapterUtils.ITEM_NO_IMAGES
    }

    override fun iNotifyDataSetChanged() {
        this.notifyDataSetChanged()
    }

    override fun notifyNewAnswersTextViews() {
        holders.forEach { it.mAnswers!!.text =
                TextUtils.getAnswersStringUpperCased(mAnswersManager.mAnswersMap[it.postNumber]!!.size)
        }
    }

    override fun getSchema(): BaseJsonSchemaImpl = mSchema

    fun setupAnswers(holder: ViewHolderAndFiles, post: Post) {
        if (mAnswersManager.mAnswersMap.containsKey(post.getNum())) {
            val answersCount = mAnswersManager.mAnswersMap[post.getNum()]!!.size
            if (answersCount == 0) {
                holder.mAnswers!!.visibility = View.GONE
                holder.mItemContainer!!.bottomPadding =
                        mView.getActivity().dimen(R.dimen.post_item_bottom_padding_no_answers)
            } else {
                holder.mAnswers!!.visibility = View.VISIBLE
                holder.mAnswers!!.text = TextUtils.getAnswersStringUpperCased(
                        mAnswersManager.mAnswersMap[post.getNum()]!!.size)
                holder.mItemContainer!!.bottomPadding = 0
            }

            holder.mAnswers!!.setOnClickListener(OnAnswersClickListener(holder.postNumber!!))
        } else {
            Log.d(LOG_TAG, "post ${post.getNum()} doesn't exits")
        }

    }

    inner class OnAnswersClickListener(val postNumber: String) : View.OnClickListener {
        override fun onClick(view: View?) {
            mAnswersManager.onAnswersClicked(postNumber)
        }
    }

    override fun openAnswersDialog() {

    }

}