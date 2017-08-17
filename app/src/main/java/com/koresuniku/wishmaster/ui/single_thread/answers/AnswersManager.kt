package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Dialog
import android.content.DialogInterface
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
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
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewView
import java.util.*
import kotlin.collections.ArrayList


class AnswersManager(val mView: AnswersManagerView) {
    val LOG_TAG: String = AnswersManager::class.java.simpleName

    val mAnswersMap: HashMap<String, ArrayList<String>> = HashMap()
    var mPreviousSchemaCount: Int = 0

    private val mAnswerDialogStack: ArrayList<AnswerDialogUnit> = ArrayList()
    private val mStubDialog: Dialog = Dialog(mView.getActivity())

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


    fun onAnswersClicked(number: String) {
        checkStubDialogShowing()

        val dialogUnit = AnswerDialogUnit(mView.getSingleThreadListViewView(), this)
        mAnswerDialogStack.add(dialogUnit)

        dialogUnit.buildDialog(mAnswersMap[number]!!)
        dialogUnit.showDialog()
    }

    private fun checkStubDialogShowing() {
        if (!mStubDialog.isShowing) mStubDialog.show()
    }


    fun openSingleAnswer(number: String) {
//        currentPostNumber = number
//        val viewArray: ArrayList<View> = ArrayList()
//        val posts: List<Post> = mView.getSchema().getPosts()!!
//
//        for (post in posts) {
//            if (post.getNum() == number) {
//                val view = mView.getDialogAdapter().getViewForDialog(
//                        posts.indexOf(post), null, FrameLayout(mView.getContext()))
//                viewArray.add(view)
//                mViews!!.add(viewArray)
//                if (mAnswersListViewAdapter != null) {
//                    val c = mDialogListView.getChildAt(0)
//                    mViewsScrolls!!.add(mViews!!.size - 2,
//                            ScrollsHolder(mDialogListView.firstVisiblePosition, c.top))
//                } else mViewsScrolls!!.add(0, ScrollsHolder(0, 0))
//                break
//            }
//        }
//
//        openDialog(true)
    }

    fun onBackPressed() {
//        if (mViews!!.size == 1 ) {
//            Log.d(LOG_TAG, "clearing...")
//            mDialog.dismiss()
//            mStubDialog.dismiss()
//            mViews!!.clear()
//            mViewsScrolls!!.clear()
//            mAnswersListViewAdapter = null
//        } else {
//            mViewsScrolls!!.removeAt(mViewsScrolls!!.size - 1)
//            mViews!!.removeAt(mViews!!.size - 1)
//
//            openDialog(false)
//        }
        if (mAnswerDialogStack.size == 1) {
            mAnswerDialogStack[0].closeDialog()
            mStubDialog.dismiss()
            mAnswerDialogStack.clear()
        } else {
            mAnswerDialogStack.last().closeDialog()
            mAnswerDialogStack.removeAt(mAnswerDialogStack.lastIndex)
        }
    }

    fun onLongBackPressed() {
//        mDialog.dismiss()
//        mStubDialog.dismiss()
//        mViews!!.clear()
//        mViewsScrolls!!.clear()
//        mAnswersListViewAdapter = null
        mAnswerDialogStack.forEach { it.closeDialog() }
        mStubDialog.dismiss()
        mAnswerDialogStack.clear()
    }

//    fun dismissDialogs() {
////        if (mViews!!.size > 0) {
////            val c = mDialogListView.getChildAt(0)
////            currentScroll = ScrollsHolder(mDialogListView.firstVisiblePosition, c.top)
////            mDialog.dismiss()
////            mStubDialog.dismiss()
////        }
//    }
//
//    fun showDialogs() {
////        if (mViews!!.size > 0) {
////            mStubDialog.show()
////            mDialog.show()
////            Log.d(LOG_TAG, "fvp: ${mViewsScrolls!![mViewsScrolls!!.size - 1].firstVisiblePosition}")
////            Log.d(LOG_TAG, "top: ${mViewsScrolls!![mViewsScrolls!!.size - 1].top}")
////            mDialogListView.setSelectionFromTop(
////                    currentScroll!!.firstVisiblePosition!!, currentScroll!!.top!!)
////        }
//    }

    fun openDialog(newAnswer: Boolean) {
//        val viewsList: List<View> = mViews!![mViews!!.size - 1]
//
//        if (mAnswersListViewAdapter == null) {
//            //mAnswersListViewAdapter = AnswersListViewAdapter(viewsList)
//            mDialogListView.adapter = mAnswersListViewAdapter
//            mDialogListView.setOnScrollListener(object : AbsListView.OnScrollListener {
//                override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {}
//
//                override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
//                    if (p1 == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                        mDialogListView.invalidateViews()
//                    }
//                }
//            })
//            mDialog.setContentView(mDialogListViewContainer)
//            mDialog.window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
//            mDialog.setOnCancelListener({ onBackPressed() })
//            mDialog.setOnKeyListener(OnLongKeyListener())
//            mStubDialog.show()
//        } else {
//            mDialog.dismiss()
//            //mAnswersListViewAdapter!!.setViewsList(viewsList)
//            mDialog.show()
//            if (!newAnswer) { mDialogListView.setSelectionFromTop(
//                        mViewsScrolls!![mViewsScrolls!!.size - 1].firstVisiblePosition!!,
//                        mViewsScrolls!![mViewsScrolls!!.size - 1].top!!)
//            }
//
//        }
//
//        if (!mDialog.isShowing) mDialog.show()
//        //else Log.d(LOG_TAG, "dialogContentView is already showing")
    }

    fun doSpanComment(view: View, number: String) {
        val comment: SpannableString = SpannableString(
                view.find<TextView>(R.id.post_comment).text)
        val pattern: Pattern = Pattern.compile(">>$number")
        val matcher: Matcher = pattern.matcher(comment)

        var start: Int
        var end: Int
        while (matcher.find()) {
            start = matcher.start()
            end = matcher.end()
            if (end + 5 <= comment.length && comment.substring(end, end + 5) == " (OP)") end += 5
//            comment.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
//                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            comment.setSpan(ForegroundColorSpan(mView.getActivity().resources.getColor(R.color.colorLinkHighlighted)),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.find<TextView>(R.id.post_comment).text = comment
        }
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