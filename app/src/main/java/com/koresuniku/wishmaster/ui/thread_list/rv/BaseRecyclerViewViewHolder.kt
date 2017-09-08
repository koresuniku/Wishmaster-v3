package com.koresuniku.wishmaster.ui.thread_list.rv

import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.domain.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils

open class BaseRecyclerViewViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView), ListViewAdapterUtils.OnThumbnailClickedCallback {
    @BindView(R.id.post_comment) lateinit var mCommentTextView: TextView

    @BindView (R.id.imagies_and_summaries_container) lateinit var imagesAndSummariesContainer: RelativeLayout
    @Nullable @BindView(R.id.image_and_summary_container) lateinit var imageAndSummaryContainer: RelativeLayout
    @BindView(R.id.image_with_summary_container_1) lateinit var imageAndSummaryContainer1: RelativeLayout
    @BindView(R.id.image_with_summary_container_2) lateinit var imageAndSummaryContainer2: RelativeLayout
    @BindView(R.id.image_with_summary_container_3) lateinit var imageAndSummaryContainer3: RelativeLayout
    @BindView(R.id.image_with_summary_container_4) lateinit var imageAndSummaryContainer4: RelativeLayout
    @BindView(R.id.image_with_summary_container_5) lateinit var imageAndSummaryContainer5: RelativeLayout
    @BindView(R.id.image_with_summary_container_6) lateinit var imageAndSummaryContainer6: RelativeLayout
    @BindView(R.id.image_with_summary_container_7) lateinit var imageAndSummaryContainer7: RelativeLayout
    @BindView(R.id.image_with_summary_container_8) lateinit var imageAndSummaryContainer8: RelativeLayout
    @Nullable @BindView(R.id.post_image) lateinit var image: ImageView
    @BindView(R.id.post_image_1) lateinit var image1: ImageView
    @BindView(R.id.post_image_2) lateinit var image2: ImageView
    @BindView(R.id.post_image_3) lateinit var image3: ImageView
    @BindView(R.id.post_image_4) lateinit var image4: ImageView
    @Nullable @BindView(R.id.post_image_5) lateinit var image5: ImageView
    @Nullable @BindView(R.id.post_image_6) lateinit var image6: ImageView
    @Nullable @BindView(R.id.post_image_7) lateinit var image7: ImageView
    @Nullable @BindView(R.id.post_image_8) lateinit var image8: ImageView
    @Nullable @BindView(R.id.webm_imageview) lateinit var webmImageView: ImageView
    @BindView(R.id.webm_imageview_1) lateinit var webmImageView1: ImageView
    @BindView(R.id.webm_imageview_2) lateinit var webmImageView2: ImageView
    @BindView(R.id.webm_imageview_3) lateinit var webmImageView3: ImageView
    @BindView(R.id.webm_imageview_4) lateinit var webmImageView4: ImageView
    @Nullable @BindView(R.id.webm_imageview_5) lateinit var webmImageView5: ImageView
    @Nullable @BindView(R.id.webm_imageview_6) lateinit var webmImageView6: ImageView
    @Nullable @BindView(R.id.webm_imageview_7) lateinit var webmImageView7: ImageView
    @Nullable @BindView(R.id.webm_imageview_8) lateinit var webmImageView8: ImageView
    @Nullable @BindView(R.id.image_summary) lateinit var summary: TextView
    @BindView(R.id.image_summary_1) lateinit var summary1: TextView
    @BindView(R.id.image_summary_2) lateinit var summary2: TextView
    @BindView(R.id.image_summary_3) lateinit var summary3: TextView
    @BindView(R.id.image_summary_4) lateinit var summary4: TextView
    @Nullable @BindView(R.id.image_summary_5) lateinit var summary5: TextView
    @Nullable @BindView(R.id.image_summary_6) lateinit var summary6: TextView
    @Nullable @BindView(R.id.image_summary_7) lateinit var summary7: TextView
    @Nullable @BindView(R.id.image_summary_8) lateinit var summary8: TextView

    var files: List<Files> = ArrayList()

    override fun onThumbnailClicked(file: Files) {

    }
}