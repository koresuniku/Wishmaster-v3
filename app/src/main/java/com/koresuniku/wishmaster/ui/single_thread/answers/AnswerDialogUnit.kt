package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewView
import org.jetbrains.anko.find


class AnswerDialogUnit(private val mView: SingleThreadListViewView,
                       private val mAnswersManager: AnswersManager,
                       private val mPostWhomAnswered: String?) {

    private val mDialogListViewContainer: FrameLayout = LayoutInflater.from(mView.getActivity())
            .inflate(R.layout.dialog_list_template, null, false) as FrameLayout
    private val mDialogListView: ListView = mDialogListViewContainer.find(R.id.dialog_listview)
    private lateinit var mAnswersListViewAdapter: AnswersListViewAdapter
    private lateinit var mDialog: Dialog

    fun buildDialog(answerNumbers: List<String>) {
        mDialog = Dialog(mView.getActivity())
        mAnswersListViewAdapter = AnswersListViewAdapter(
                mView, getSchemaForAdapter(answerNumbers), mAnswersManager, mPostWhomAnswered)
        mDialogListView.adapter = mAnswersListViewAdapter

        mDialog.setContentView(mDialogListViewContainer)
        mDialog.window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        mDialog.setOnCancelListener({ mAnswersManager.onBackPressed() })
        mDialog.setOnKeyListener(mAnswersManager.OnLongKeyListener())
    }

    fun showDialog() {
        mDialog.show()
    }

    fun closeDialog() {
        mDialog.dismiss()
    }

    private fun getSchemaForAdapter(answerNumbers: List<String>): BaseJsonSchemaImpl {
        val resultSchema = BaseJsonSchemaImpl()
        val resultPostList = ArrayList<Post>()

        mView.getSchema().getPosts()!!
                .filter { answerNumbers.contains(it.getNum()) }
                .forEach { resultPostList.add(it) }

        resultSchema.setPosts(resultPostList)

        return resultSchema
    }
}