package com.koresuniku.wishmaster.ui.single_thread

import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.http.thread_list_api.model.Thread
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.INotifyableListViewAdapter
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView
import org.jetbrains.anko.sdk25.coroutines.onClick

class SingleThreadListViewAdapter(val mView: SingleThreadListViewView) :
        BaseAdapter(), INotifyableListViewAdapter, AnswersHolderView {
    val LOG_TAG: String = SingleThreadListViewAdapter::class.java.simpleName

    val ITEM_NO_IMAGES: Int = 0
    val ITEM_SINGLE_IMAGE: Int = 1
    val ITEM_MULTIPLE_IMAGES: Int = 2

    val mHandler = Handler()
    var holdersCounter = 0
    val holders: ArrayList<ViewHolder> = ArrayList()
    val mAnswersHolder: AnswersHolder = AnswersHolder(this)

    init {
        mAnswersHolder.initAnswersMap()
        mAnswersHolder.appointAnswersToPosts()
    }

    inner class ViewHolder : FilesListViewViewHolder() {
        var mItemContainer: RelativeLayout? = null
        var mNumberAndTimeInfo: TextView? = null
        var mCommentTextView: NoScrollTextView? = null
        var mAnswers: TextView? = null

        var viewType: Int? = null
        var code: Int = -1
        var postNumber: String? = null

        init {
            files = ArrayList()
        }
    }

    fun inflateCorrectConvertView(position: Int, parent: ViewGroup): View {
        when (getItemViewType(position)) {
            ITEM_NO_IMAGES -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.post_item_no_images, parent, false)
            }
            ITEM_SINGLE_IMAGE -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.post_item_single_image, parent, false)
            }
            ITEM_MULTIPLE_IMAGES -> {
                return LayoutInflater.from(mView.getActivity())
                        .inflate(R.layout.post_item_multiple_images, parent, false)
            }
        }
        return View(mView.getActivity())
    }

    fun getViewHolderInstance(itemView: View, viewType: Int): ViewHolder {
        val holder: ViewHolder = ViewHolder()
        holder.viewType = viewType

        holder.mItemContainer = itemView.findViewById(R.id.post_item_container) as RelativeLayout
        holder.mNumberAndTimeInfo = itemView.findViewById(R.id.post_number_and_time_info) as TextView
        holder.mCommentTextView = itemView.findViewById(R.id.post_comment) as NoScrollTextView
        holder.mAnswers = itemView.findViewById(R.id.answers) as TextView
        if (viewType == ITEM_SINGLE_IMAGE) {
            holder.image = itemView.findViewById(R.id.post_image) as ImageView
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


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        var holder: ViewHolder
        val post: Post = mView.getSchema().getPosts()!![position]
        val files: List<Files> = post.getFiles()

        if (convertView == null) {
            convertView = inflateCorrectConvertView(position, parent!!)
            holder = getViewHolderInstance(convertView, getItemViewType(position))
            holder.code = holdersCounter++
            holder.files = files
            holder.postNumber = post.getNum()
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            if (holder.viewType != getItemViewType(position)) {
                convertView = inflateCorrectConvertView(position, parent!!)
                //Log.d(LOG_TAG, "reusing holder: " + holder.code)
                val code = holder.code
                holders.filter { it.code == code }.forEach { holders.removeAt(holders.indexOf(it)) }
                holder = getViewHolderInstance(convertView, getItemViewType(position))
                holder.code = code
                holder.files = files
                holder.postNumber = post.getNum()
                convertView.tag = holder
            }
        }

        holder.mItemContainer!!.onClick { }

//        holder.mItemContainer!!.setOnClickListener {object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//
//            }
//        }}

        holder.mItemContainer!!.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                mView.showPostDialog(position)
                return false
            }
        })

        holder.mNumberAndTimeInfo!!.text =
                TextUtils.getNumberAndTimeInfoSpannableString(mView.getActivity(), position, post)
        holder.mCommentTextView!!.text =
                Html.fromHtml(post.getComment())
        if (mAnswersHolder.mAnswersMap.containsKey(post.getNum())) {
            holder.mAnswers!!.text =
                    TextUtils.getAnswersStringUpperCased(mAnswersHolder.mAnswersMap[post.getNum()]!!.size)
        } else {
            Log.d(LOG_TAG, "post ${post.getNum()} doesn't exits")
        }


        holder.files = post.getFiles()
        ListViewAdapterUtils.setupImages(mView.getActivity(), holder, false, true)


        holders.add(holder)
        return convertView
    }

    override fun getItem(position: Int): Any {
        return mView.getSchema().getPosts()!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mView.getSchema().getPosts()!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val size = mView.getSchema().getPosts()!![position].getFiles().size
        if (size == 0) return ITEM_NO_IMAGES
        if (size == 1) return ITEM_SINGLE_IMAGE
        if (size > 1) return ITEM_MULTIPLE_IMAGES
        return ITEM_NO_IMAGES
    }

    override fun iNotifyDataSetChanged() {
        this.notifyDataSetChanged()
    }

    override fun notifyNewAnswersTextViews() {
        holders.forEach { it.mAnswers!!.text =
                TextUtils.getAnswersStringUpperCased(mAnswersHolder.mAnswersMap[it.postNumber]!!.size)}
    }

    override fun getSchema(): IBaseJsonSchemaImpl {
        return mView.getSchema()
    }
}