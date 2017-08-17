package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Dialog
import android.content.DialogInterface
import android.text.Html
import android.util.Log
import android.view.View
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import org.jetbrains.anko.find
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.widget.*
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.ui.gallery.GalleryVisibilityEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList


class AnswersManager(val mView: AnswersManagerView) {
    val LOG_TAG: String = AnswersManager::class.java.simpleName

    val mAnswersMap: HashMap<String, ArrayList<String>> = HashMap()
    var mPreviousSchemaCount: Int = 0

    private val mAnswerDialogStack: ArrayList<AnswerDialogUnit> = ArrayList()
    private val mBackgroundTintDialog: Dialog = Dialog(mView.getActivity())

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event.anEvent) {
            LifecycleEvent.onStart -> EventBus.getDefault().register(this)
            LifecycleEvent.onStop -> EventBus.getDefault().unregister(this)
        }
    }

    fun initAnswersMap() {
        Log.d(LOG_TAG, "initAnswerMap:")
        for (post: Post in mView.getSchema().getPosts()!!) {
            mAnswersMap.put(post.getNum(), ArrayList())
        }
    }

    fun assignAnswersToPosts() {
        val pattern: Pattern = Pattern.compile(">>[0-9]+")
        var matcher: Matcher
        val subSchema = BaseJsonSchemaImpl()
        val currentSchemaCount = mView.getSchema().getPosts()!!.size

        subSchema.setPosts(mView.getSchema().getPosts()!!.subList(
                mPreviousSchemaCount, currentSchemaCount))
        subSchema.getPosts()!!.forEach { mAnswersMap.put(it.getNum(), ArrayList()) }

        var post: Post
        subSchema.getPosts()!!.forEach {
            post = it
            matcher = pattern.matcher(Html.fromHtml(post.getComment()))
            while (matcher.find()) {
                val numberWhomAnswered = matcher.group().substring(2, matcher.group().length)
                if (mAnswersMap.containsKey(numberWhomAnswered)) {
                    val answerList = mAnswersMap[numberWhomAnswered]
                    if (!answerList!!.contains(post.getNum())) {
                        answerList.add(post.getNum())
                        mAnswersMap.put(numberWhomAnswered, answerList)
                    }
                }
            }
        }

        mPreviousSchemaCount = currentSchemaCount

        if (subSchema.getPosts()!!.isNotEmpty()) mView.notifyNewAnswersTextViews()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGalleryVisibilityEvent(event: GalleryVisibilityEvent) {
        if (mAnswerDialogStack.isNotEmpty()) {
            when (event.visibitity) {
                GalleryVisibilityEvent.IS_HIDDEN -> {
                    mBackgroundTintDialog.show()
                    mAnswerDialogStack.forEach { it.showDialog() }
                }
                GalleryVisibilityEvent.IS_SHOWN -> {
                    mAnswerDialogStack.forEach { it.closeDialog() }
                    mBackgroundTintDialog.dismiss()
                }
            }
        }
    }


    fun onAnswersClicked(number: String) {
        checkBackgroundDialogShowing()

        val dialogUnit = AnswerDialogUnit(mView.getSingleThreadListViewView(), this, number)
        mAnswerDialogStack.add(dialogUnit)

        dialogUnit.buildDialog(mAnswersMap[number]!!)
        dialogUnit.showDialog()
    }

    private fun checkBackgroundDialogShowing() {
        if (!mBackgroundTintDialog.isShowing) mBackgroundTintDialog.show()
    }

    fun openSingleAnswer(number: String) {
        checkBackgroundDialogShowing()

        val dialogUnit = AnswerDialogUnit(mView.getSingleThreadListViewView(), this, null)
        mAnswerDialogStack.add(dialogUnit)

        dialogUnit.buildDialog(arrayListOf(number))
        dialogUnit.showDialog()
    }

    fun onBackPressed() {
        if (mAnswerDialogStack.size == 1) {
            mAnswerDialogStack[0].closeDialog()
            mBackgroundTintDialog.dismiss()
            mAnswerDialogStack.clear()
        } else {
            mAnswerDialogStack.last().closeDialog()
            mAnswerDialogStack.removeAt(mAnswerDialogStack.lastIndex)
        }
    }

    fun onLongBackPressed() {
        mAnswerDialogStack.forEach { it.closeDialog() }
        mBackgroundTintDialog.dismiss()
        mAnswerDialogStack.clear()
    }



    inner class OnLongKeyListener : DialogInterface.OnKeyListener {
        var times: Int = 0
        var beforeTime: Long = 0L
        var afterTime: Long = 0L

        override fun onKey(p0: DialogInterface?, p1: Int, p2: KeyEvent?): Boolean {
            if (p1 == KeyEvent.KEYCODE_BACK) {
                Log.d(LOG_TAG, "keycode back")
                times++
                if (times == 1) {
                    beforeTime = System.currentTimeMillis()
                }
                if (times == 2) {
                    afterTime = System.currentTimeMillis()
                    if (afterTime - beforeTime > 400) onLongBackPressed()
                    else onBackPressed()
                    times = 0
                    beforeTime = 0L
                    afterTime = 0L
                }
                return true
            }

            return false
        }
    }

}