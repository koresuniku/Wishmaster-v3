package com.koresuniku.wishmaster.ui.thread_list.rv

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.widget.NoScrollTextView

class ThreadListRecyclerViewViewHolder(itemView: View?, viewType: Int) :
        BaseRecyclerViewViewHolder(itemView, viewType) {
    @BindView (R.id.thread_item_container) lateinit var mItemContainer: RelativeLayout
    @BindView (R.id.post_subject) lateinit var mSubjectTextView: TextView
    @BindView (R.id.answers) lateinit var mPostsAndFilesInfo: TextView
}