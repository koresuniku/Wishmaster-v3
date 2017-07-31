package com.koresuniku.wishmaster.ui.gallery

import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.text.TextUtils
import org.jetbrains.anko.find
import org.w3c.dom.Text

class GalleryActionBarUnit(mView: ActionBarView) : ActionBarUnit(mView, true, false) {
    override var LOG_TAG: String = GalleryActionBarUnit::class.java.simpleName

    var mBackButton: ImageView? = null
    var mTitle: TextView? = null
    var mSubtitle: TextView? = null

    var mFile: Files? = null
    var mIndexOfFile: Int? = null
    var mFilesCount: Int? = null
    var title: String? = null
    var subtitle: String? = null


    override fun postSetupActionBar() {
        mBackButton = mToolbar!!.find(R.id.back_button)
        mBackButton!!.setOnClickListener { Log.d(LOG_TAG, "back button pressed"); mView.onBackPressedOverridden() }
        mBackButton!!.bringToFront()
    }

    override fun getIdRes(): Int {
        return R.layout.gallery_action_bar_layout
    }

    fun onPageChanged(file: Files, indexOfFile: Int, filesCount: Int) {
        setupTitleAndSubtitle(file, indexOfFile, filesCount)
    }

    override fun setSupportActionBarAndTitle() {
        //Do nothing
    }

    fun setupTitleAndSubtitle(file: Files, indexOfFile: Int, filesCount: Int) {
        mTitle = mToolbar!!.find(R.id.title)
        mSubtitle = mToolbar!!.find(R.id.subtitle)

        mFile = file
        mIndexOfFile = indexOfFile
        mFilesCount = filesCount
        title = file.getDisplayName()
        subtitle = TextUtils.getSubtitleStringForGalleryToolbar(
                mFile!!, mIndexOfFile!!, mFilesCount!!)

        mTitle!!.text = title
        mSubtitle!!.text = subtitle
    }

}