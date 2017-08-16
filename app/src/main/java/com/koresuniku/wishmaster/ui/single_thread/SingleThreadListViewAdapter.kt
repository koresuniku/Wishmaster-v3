package com.koresuniku.wishmaster.ui.single_thread

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.controller.DialogManager
import com.koresuniku.wishmaster.ui.gallery.GalleryActionBarUnit
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder
import com.koresuniku.wishmaster.ui.controller.view_interface.IDialogAdapter
import com.koresuniku.wishmaster.ui.controller.view_interface.INotifyableListViewAdapter
import com.koresuniku.wishmaster.ui.gallery.GalleryOnPageChangeListener
import com.koresuniku.wishmaster.ui.gallery.GalleryPagerAdapter
import com.koresuniku.wishmaster.ui.gallery.GalleryPagerView
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersManager
import com.koresuniku.wishmaster.ui.single_thread.answers.AnswersHolderView
import com.koresuniku.wishmaster.ui.text.*
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.ui.text.comment_leading_margin_span.CommentLeadingMarginSpan2
import com.koresuniku.wishmaster.ui.text.comment_link_movement_method.CommentLinkMovementMethod
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView
import com.pixplicity.htmlcompat.HtmlCompat
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


open class SingleThreadListViewAdapter(val mView: SingleThreadListViewView) :
        BaseAdapter(), INotifyableListViewAdapter, AnswersHolderView, IDialogAdapter, ActionBarView,
        DialogManager.GalleryVisibilityListener {
    val LOG_TAG: String = SingleThreadListViewAdapter::class.java.simpleName

    val ITEM_NO_IMAGES: Int = 0
    val ITEM_SINGLE_IMAGE: Int = 1
    val ITEM_MULTIPLE_IMAGES: Int = 2

    val holders: ArrayList<ViewHolderAndFiles> = ArrayList()
    val mAnswersHolder: AnswersManager = AnswersManager(this)

    val mGalleryActionBarUnit: GalleryActionBarUnit = GalleryActionBarUnit(this)
    var mGalleryPager: ViewPager? = null
    var mGalleryPagerAdapter: GalleryPagerAdapter? = null
    var mGalleryOnPageChangeListener: GalleryOnPageChangeListener? = null

    init {
        mAnswersHolder.initAnswersMap()
        mAnswersHolder.appointAnswersToPosts()
    }

    inner class ViewHolderAndFiles : CommentAndFilesListViewViewHolder(), GalleryPagerView {
        var mItemContainer: RelativeLayout? = null
        var mNumberAndTimeInfo: TextView? = null
        var mAnswers: TextView? = null
        var postNumber: String? = null

        override fun showImageOrVideo(file: Files) {
            mAnswersHolder.dismissDialogs()

            UIVisibilityManager.setBarsTranslucent(mView.getActivity(), true)
            mView.getGalleryLayoutContainer().visibility = View.VISIBLE

            val filesList = getFilesList()
            val currentPosition = filesList.indexOf(file)
            mGalleryActionBarUnit.setupTitleAndSubtitle(file, currentPosition, filesList.count())

            mGalleryPager = mView.getViewPager()
            mGalleryPagerAdapter = GalleryPagerAdapter(
                    mView.getAppCompatActivity().supportFragmentManager, this,
                    filesList, currentPosition)
            mGalleryPager!!.adapter = mGalleryPagerAdapter
            mGalleryPager!!.offscreenPageLimit = 1
            mGalleryPager!!.currentItem = currentPosition
            mGalleryOnPageChangeListener = GalleryOnPageChangeListener(this)
            mGalleryPager!!.addOnPageChangeListener(mGalleryOnPageChangeListener)
        }

        override fun getGalleryActionBar(): GalleryActionBarUnit {
            return mGalleryActionBarUnit
        }

        override fun onGalleryHidden() {
            mAnswersHolder.showDialogs()
        }

        override fun getViewPager(): ViewPager {
            return mGalleryPager!!
        }

        override fun getActivity(): Activity {
            return mView.getActivity()
        }

        override fun onPageChanged(newPosition: Int) {
            mGalleryPagerAdapter!!.onPageChanged(newPosition)
            val filesList = getFilesList()
            val currentPosition = newPosition
            val file = filesList[newPosition]
            mGalleryActionBarUnit.onPageChanged(file, currentPosition, filesList.count())
        }


        init {
            files = ArrayList()
        }
    }

    fun getFilesList(): List<Files> {
        val filesList: ArrayList<Files> = ArrayList()
        for (post in mView.getSchema().getPosts()!!) {
            filesList += post.getFiles()
        }
        return filesList
    }

    override fun getContext(): Context {
        return mView.getActivity()
    }

    override fun getActivity(): Activity {
        return mView.getActivity()
    }

    override fun getToolbarContainer(): FrameLayout {
        return mView.getActivity().find<FrameLayout>(R.id.gallery_toolbar_container)
    }

    override fun getAppCompatActivity(): AppCompatActivity {
        return mView.getAppCompatActivity()
    }

    override fun onGalleryShown() {
        mAnswersHolder.dismissDialogs()
    }

    override fun onGalleryHidden() {
        mAnswersHolder.showDialogs()
    }

    override fun setupActionBarTitle() {

    }

    override fun onBackPressedOverridden(): Boolean {
        if (mView.getGalleryLayoutContainer().visibility == View.VISIBLE) {
            UIVisibilityManager.setBarsTranslucent(mView.getActivity(), false)
            mView.getGalleryLayoutContainer().visibility = View.GONE

            mGalleryPager!!.clearOnPageChangeListeners()
            mGalleryPagerAdapter!!.onBackPressed()
            return true
        }


        return false
    }

    fun onConfigurationChanged(configuration: Configuration) {
        if (mView.getGalleryLayoutContainer().visibility == View.VISIBLE) {
            mGalleryActionBarUnit.onConfigurationChanged(configuration)
            mGalleryActionBarUnit.setupTitleAndSubtitle(mGalleryActionBarUnit.mFile!!,
                mGalleryActionBarUnit.mIndexOfFile!!, mGalleryActionBarUnit.mFilesCount!!)
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

    fun getViewHolderInstance(itemView: View, viewType: Int): ViewHolderAndFiles {
        val holder: ViewHolderAndFiles = ViewHolderAndFiles()
        holder.viewType = viewType

        holder.mItemContainer = itemView.findViewById(R.id.post_item_container) as RelativeLayout
        holder.mNumberAndTimeInfo = itemView.findViewById(R.id.post_number_and_time_info) as TextView
        holder.mCommentTextView = itemView.findViewById(R.id.post_comment) as NoScrollTextView
        holder.mAnswers = itemView.findViewById(R.id.answers) as TextView
        if (viewType == ITEM_SINGLE_IMAGE) {
            holder.imageAndSummaryContainer = itemView.findViewById(R.id.image_and_summary_container) as RelativeLayout
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


    override fun getViewTypeCount(): Int {
        return 3
    }

    open fun getPost(position: Int): Post {
        return mView.getSchema().getPosts()!![position]
    }

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
        ListViewAdapterUtils.setupImages(mView.getActivity(), holder, false, true)

        //Log.d(LOG_TAG, "viewholder.size: ${holders.size}")

        holder.mItemContainer!!.setOnLongClickListener {
            mView.showPostDialog(position)
            false
        }

        val mNumberAndTimeSpannable =
                TextUtils.getNumberAndTimeInfoSpannableString(mView.getActivity(), position, post)
        holder.mNumberAndTimeInfo!!.setText(mNumberAndTimeSpannable, TextView.BufferType.SPANNABLE)
        //Log.d(LOG_TAG, "raw html: ${post.getComment()}")
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
                    CommentLinkMovementMethod(mView.getActivity(), mAnswersHolder)

            if (holder.viewType == ListViewAdapterUtils.ITEM_SINGLE_IMAGE) {
                holder.mCommentTextView!!.post {
                    var spannable = SpannableString(HtmlCompat.fromHtml(
                            mView.getActivity(), commentDocument.html(), 0,
                            null, SpanTagHandlerCompat(mView.getActivity())))
                    val textViewWidth = CommentLeadingMarginSpan2.calculateCommentTextViewWidthInPx(holder)

                    var end: Int = 0
                    var overallHeightOfLines: Int = 0
                    val imageContainerHeight: Int = UIUtils.convertDpToPixel(
                            CommentLeadingMarginSpan2.calculateImageContainerHeightInDp(holder)).toInt()
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

//                    val layout: StaticLayout = StaticLayout(spannable.toString(), myTextPaint, textViewWidth,
//                                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false)
//                    if (layout.lineCount > 0) {
//                            for (lineIndex in 0..layout.lineCount - 1) {
//                                val aLine = spannable.substring(layout.getLineStart(lineIndex), layout.getLineEnd(lineIndex))
//                                Log.d(LOG_TAG, "line $lineIndex is: $aLine")
//                            }
//                            val imageContainerHeight: Int = UIUtils.convertDpToPixel(
//                                    CommentLeadingMarginSpan2.calculateImageContainerHeightInDp(holder)).toInt()
//
//                            for (lineIndex in 0..layout.lineCount - 1) {
//                                if (layout.getLineBottom(lineIndex) > imageContainerHeight) {
//                                    end = layout.getLineEnd(lineIndex)
//                                    val spannableStringBuilder = SpannableStringBuilder(spannable)
//                                    if (spannable.substring(end, end + 1) != "\n" &&
//                                            spannable.substring(end, end + 1) != "\r") {
//                                        if (spannable.substring(end, end + 1) == " ") {
//                                            spannableStringBuilder.replace(end, end + 1, "\n")
//                                        } else {
//                                            spannableStringBuilder.insert(end, "\n")
//                                        }
//                                    }
//                                    spannable = SpannableString(spannableStringBuilder)
//                                    break
//                                }
//                            }
//
//                        }

                    //Log.d(LOG_TAG, "spanem: ${spannable.substring(0, end)}")

                    spannable.setSpan(CommentLeadingMarginSpan2(
                            CommentLeadingMarginSpan2.calculateLeadingMarginWidthInPx(holder)),
                            0, if (end == 0) spannable.length else end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    //}

                    holder.mCommentTextView!!.text = spannable

                    holder.mCommentTextView!!.requestLayout()

                    //set left margin of desirable width
//                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                params.leftMargin = holder.imageContainerHeight!!
////                params.leftMargin = 200
//                params.addRule(RelativeLayout.BELOW, holder.mNumberAndTimeInfo!!.id)
//                holder.mCommentTextView!!.layoutParams = params
//                if (holder.commentTextViewOnGlobalLayoutListener != null)
//                    holder.mCommentTextView!!.viewTreeObserver.removeOnGlobalLayoutListener(
//                            holder.commentTextViewOnGlobalLayoutListener)
//
//                //add onGlobalLayoutListener
//                holder.mCommentTextView!!.viewTreeObserver.addOnGlobalLayoutListener(
//                        if (holder.commentTextViewOnGlobalLayoutListener != null)
//                            holder.commentTextViewOnGlobalLayoutListener
//                        else CommentTextViewOnGlobalLayoutListener(holder,
//                                SpannableString(HtmlCompat.fromHtml(
//                                mView.getActivity(), commentDocument.html(), 0,
//                                null, SpanTagHandlerCompat(mView.getActivity())))))
                }
            } else {
                holder.mCommentTextView!!.text = HtmlCompat.fromHtml(
                        mView.getActivity(), commentDocument.html(), 0,
                        null, SpanTagHandlerCompat(mView.getActivity()))
            }

        holder.postNumber = post.getNum()
        setupAnswers(holder, post)


        holder.mAnswers!!.bringToFront()

        return convertView
    }

    class CommentTextViewOnGlobalLayoutListener(
            val holder: CommentAndFilesListViewViewHolder, val commentSpannable: Spannable) :
            ViewTreeObserver.OnGlobalLayoutListener {
        val LOG_TAG: String = CommentTextViewOnGlobalLayoutListener::class.java.simpleName

        override fun onGlobalLayout() {

            holder.mCommentTextView!!.viewTreeObserver.removeGlobalOnLayoutListener(this)

            Log.d(LOG_TAG, "layout width on global: ${holder.mCommentTextView!!.layout.width}")
//
//            //when textview layout is drawn, get the line end to spanify only the needed text
//            val charCount = holder.mCommentTextView!!.layout.getLineEnd(Math.min(
//                    holder.mCommentTextView!!.layout.lineCount - 1,
//                    CommentLeadingMarginSpan.computeLinesToBeSpanned(holder)))
//                    //6))
//            if (charCount <= commentSpannable.length) {
//                commentSpannable.setSpan(CommentLeadingMarginSpan(holder),
//                        0, charCount, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            }
//
//            //set the left margin back to zero
//            (holder.mCommentTextView!!.layoutParams as RelativeLayout.LayoutParams).leftMargin = 0
//            holder.mCommentTextView!!.text = commentSpannable
        }
    }

    override fun getViewForDialog(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolderAndFiles
        val post: Post = mView.getSchema().getPosts()!![position]

        if (convertView == null) {
            convertView = inflateCorrectConvertView(position, parent)
            holder = getViewHolderInstance(convertView, getItemViewType(position))
            holder.postNumber = post.getNum()
            convertView.tag = holder
            holders.add(holder)
        } else {
            holder = convertView.tag as ViewHolderAndFiles
            holder.postNumber = post.getNum()
        }

        holder.files = post.getFiles()
        ListViewAdapterUtils.setupImages(mView.getActivity(), holder, true, true)

        holder.mItemContainer!!.setOnLongClickListener {
            mView.showPostDialog(position)
            false
        }

        val mNumberAndTimeSpannable =
                TextUtils.getNumberAndTimeInfoSpannableString(mView.getActivity(), position, post)
        holder.mNumberAndTimeInfo!!.setText(mNumberAndTimeSpannable, TextView.BufferType.SPANNABLE)
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

        holder.mCommentTextView!!.text = HtmlCompat.fromHtml(
                mView.getActivity(), commentDocument.html(), 0,
                null, SpanTagHandlerCompat(mView.getActivity()))
        holder.mCommentTextView!!.linksClickable = false
        holder.mCommentTextView!!.movementMethod = CommentLinkMovementMethod(mView.getActivity(), mAnswersHolder)
        holder.postNumber = post.getNum()
        setupAnswers(holder, post)



        holder.mAnswers!!.bringToFront()

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

    fun setupAnswers(holder: ViewHolderAndFiles, post: Post) {
        if (mAnswersHolder.mAnswersMap.containsKey(post.getNum())) {
            val answersCount = mAnswersHolder.mAnswersMap[post.getNum()]!!.size
            if (answersCount == 0) {
                holder.mAnswers!!.visibility = View.GONE
                holder.mItemContainer!!.bottomPadding =
                        mView.getActivity().dimen(R.dimen.post_item_bottom_padding_no_answers)
            } else {
                holder.mAnswers!!.visibility = View.VISIBLE
                holder.mAnswers!!.text = TextUtils.getAnswersStringUpperCased(
                        mAnswersHolder.mAnswersMap[post.getNum()]!!.size)
                holder.mItemContainer!!.bottomPadding = 0
            }

            holder.mAnswers!!.setOnClickListener(OnAnswersClickListener(holder.postNumber!!))
        } else {
            Log.d(LOG_TAG, "post ${post.getNum()} doesn't exits")
        }

    }

    inner class OnAnswersClickListener(val postNumber: String) : View.OnClickListener {
        override fun onClick(p0: View?) {
            mAnswersHolder.onAnswersClicked(postNumber)
        }
    }

    override fun openAnswersDialog() {

    }

    override fun getDialogAdapter(): IDialogAdapter {
        return this
    }
}