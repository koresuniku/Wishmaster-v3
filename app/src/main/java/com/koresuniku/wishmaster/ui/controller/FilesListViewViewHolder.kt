package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.koresuniku.wishmaster.http.thread_list_api.model.Files

abstract class FilesListViewViewHolder(var activity: Activity) {
    var mCommentTextView: TextView? = null

    var imageAndSummaryContainer: RelativeLayout? = null
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

    var files: List<Files>? = null
    var viewType: Int? = null

}