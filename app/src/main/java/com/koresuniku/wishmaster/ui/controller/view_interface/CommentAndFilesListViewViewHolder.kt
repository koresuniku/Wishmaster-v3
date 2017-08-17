package com.koresuniku.wishmaster.ui.controller.view_interface

import android.app.Activity
import android.widget.TextView
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder

abstract class CommentAndFilesListViewViewHolder : FilesListViewViewHolder() {
    var mCommentTextView: TextView? = null

    open var imageContainerHeight: Int? = null

    abstract fun getActivity(): Activity


}