package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Activity
import com.koresuniku.wishmaster.http.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewAdapter
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewView
import org.greenrobot.eventbus.EventBus

class AnswersListViewAdapter(mView: SingleThreadListViewView,
                             mSchema: BaseJsonSchemaImpl,
                             override var mAnswersManager: AnswersManager) :
        SingleThreadListViewAdapter(mView, mSchema, true) {
    override val LOG_TAG: String = AnswersListViewAdapter::class.java.simpleName

    override fun initAnswersManager() {}

    override fun onThumbnailClickedEvent(event: OnThumbnailClickedEvent) {}

    override fun getCount(): Int = mSchema.getPosts()!!.size

    override fun getActivity(): Activity = mView.getActivity()

    override fun onThumbnailClicked(file: Files) {
        EventBus.getDefault().post(OnThumbnailClickedEvent(file))
    }
}