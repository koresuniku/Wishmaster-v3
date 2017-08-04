package com.koresuniku.wishmaster.ui.single_thread.answers

import android.app.Dialog
import android.content.DialogInterface
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import org.jetbrains.anko.find
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.KeyEvent
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList


class AnswersManager(val mView: AnswersHolderView) {
    val LOG_TAG: String = AnswersManager::class.java.simpleName

    val mAnswersMap: HashMap<String, ArrayList<String>> = HashMap()
    var mPreviousSchema: IBaseJsonSchemaImpl? = null
    var mViews: ArrayList<ArrayList<View>>? = ArrayList()
    var mViewsScrolls: ArrayList<ScrollsHolder>? = ArrayList()

    val mDialog: Dialog = Dialog(mView.getContext())
    val mStubDialog: Dialog = Dialog(mView.getContext())
    var currentPostNumber: String? = null

    val mDialogListViewContainer: FrameLayout = LayoutInflater.from(mView.getContext())
            .inflate(R.layout.dialog_list_template, null, false) as FrameLayout
    val mDialogListView: ListView = mDialogListViewContainer.find<ListView>(R.id.dialog_listview)
    var mAnswersListViewAdapter: AnswersListViewAdapter? = null
    var currentScroll: ScrollsHolder? = null

    fun initAnswersMap() {
        for (post: Post in mView.getSchema().getPosts()!!) {
            mAnswersMap.put(post.getNum(), ArrayList())
        }
    }

    fun appointAnswersToPosts() {
        val pattern: Pattern = Pattern.compile(">>[0-9]+")
        var matcher: Matcher
        val schema: IBaseJsonSchemaImpl

        if (mPreviousSchema != null) {
            schema = IBaseJsonSchemaImpl()
            schema.setPosts(mView.getSchema().getPosts()!!.subList(
                    mPreviousSchema!!.getPosts()!!.size, mView.getSchema().getPosts()!!.size))
            //Log.d(LOG_TAG, "new posts on schema: " + schema.getPosts()!!.size)
            for (post: Post in schema.getPosts()!!) {
                //Log.d(LOG_TAG, "putting post: ${post.getNum()}")
                mAnswersMap.put(post.getNum(), ArrayList())
            }
        } else schema = mView.getSchema()

        for (post: Post in schema.getPosts()!!) {
            matcher = pattern.matcher(Html.fromHtml(post.getComment()))
            while (matcher.find()) {
                val group: String = matcher.group()
                //Log.d(LOG_TAG, "found $group")
                if (mAnswersMap.containsKey(group.substring(2, group.length))) {
                    val answersList = mAnswersMap[group.substring(2, group.length)]
                    if (!answersList!!.contains(post.getNum())) {
                        answersList.add(post.getNum())
                        mAnswersMap.put(group.substring(2, group.length), answersList)
                    }
                }
            }
        }

        //Log.d(LOG_TAG, "mAnswersMap: $mAnswersMap")

        if (mPreviousSchema != null) mView.notifyNewAnswersTextViews()
    }

    fun savePreviousSchema(schema: IBaseJsonSchemaImpl) {
        mPreviousSchema = schema
    }

    fun onAnswersClicked(number: String) {
        currentPostNumber = number
        val answersArray: ArrayList<String> = mAnswersMap[number]!!
        val viewArray: ArrayList<View> = ArrayList()
        val posts: List<Post> = mView.getSchema().getPosts()!!

        var view: View
        for (answer in answersArray) {
            val position: Int = posts
                    .firstOrNull { it.getNum() == answer }
                    ?.let { posts.indexOf(it) }
                    ?: -1
            view = mView.getDialogAdapter().getViewForDialog(position, null, FrameLayout(mView.getContext()))
            doSpanComment(view, number)
            viewArray.add(view)
        }
        mViews!!.add(viewArray)

        if (mAnswersListViewAdapter != null) {
            val c = mDialogListView.getChildAt(0)
            mViewsScrolls!!.add(mViews!!.size - 2,
                    ScrollsHolder(mDialogListView.firstVisiblePosition, c.top))
        } else mViewsScrolls!!.add(0, ScrollsHolder(0, 0))

        openDialog(true)
    }


    fun openSingleAnswer(number: String) {
        currentPostNumber = number
        val viewArray: ArrayList<View> = ArrayList()
        val posts: List<Post> = mView.getSchema().getPosts()!!

        for (post in posts) {
            if (post.getNum() == number) {
                val view = mView.getDialogAdapter().getViewForDialog(
                        posts.indexOf(post), null, FrameLayout(mView.getContext()))
                viewArray.add(view)
                mViews!!.add(viewArray)
                if (mAnswersListViewAdapter != null) {
                    val c = mDialogListView.getChildAt(0)
                    mViewsScrolls!!.add(mViews!!.size - 2,
                            ScrollsHolder(mDialogListView.firstVisiblePosition, c.top))
                } else mViewsScrolls!!.add(0, ScrollsHolder(0, 0))
                break
            }
        }

        openDialog(true)
    }

    fun onBackPressed() {
        if (mViews!!.size == 1 ) {
            Log.d(LOG_TAG, "clearing...")
            mDialog.dismiss()
            mStubDialog.dismiss()
            mViews!!.clear()
            mViewsScrolls!!.clear()
            mAnswersListViewAdapter = null
        } else {
            mViewsScrolls!!.removeAt(mViewsScrolls!!.size - 1)
            mViews!!.removeAt(mViews!!.size - 1)

            openDialog(false)
        }
    }

    fun onLongBackPressed() {
        mDialog.dismiss()
        mStubDialog.dismiss()
        mViews!!.clear()
        mViewsScrolls!!.clear()
        mAnswersListViewAdapter = null
    }

    fun dismissDialogs() {
        if (mViews!!.size > 0) {
            val c = mDialogListView.getChildAt(0)
            currentScroll = ScrollsHolder(mDialogListView.firstVisiblePosition, c.top)
            mDialog.dismiss()
            mStubDialog.dismiss()
        }
    }

    fun showDialogs() {
        if (mViews!!.size > 0) {
            mStubDialog.show()
            mDialog.show()
            Log.d(LOG_TAG, "fvp: ${mViewsScrolls!![mViewsScrolls!!.size - 1].firstVisiblePosition}")
            Log.d(LOG_TAG, "top: ${mViewsScrolls!![mViewsScrolls!!.size - 1].top}")
            mDialogListView.setSelectionFromTop(
                    currentScroll!!.firstVisiblePosition!!, currentScroll!!.top!!)
        }
    }

    fun openDialog(newAnswer: Boolean) {
        val viewsList: List<View> = mViews!![mViews!!.size - 1]

        if (mAnswersListViewAdapter == null) {
            mAnswersListViewAdapter = AnswersListViewAdapter(viewsList)
            mDialogListView.adapter = mAnswersListViewAdapter
            mDialogListView.setOnScrollListener(object : AbsListView.OnScrollListener {
                override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {}

                override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
                    if (p1 == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        mDialogListView.invalidateViews()
                    }
                }
            })
            mDialog.setContentView(mDialogListViewContainer)
            mDialog.window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            mDialog.setOnCancelListener({ onBackPressed() })
            mDialog.setOnKeyListener(OnLongKeyListener())
            mStubDialog.show()
        } else {
            mDialog.dismiss()
            mAnswersListViewAdapter!!.setViewsList(viewsList)
            mDialog.show()
            if (!newAnswer) { mDialogListView.setSelectionFromTop(
                        mViewsScrolls!![mViewsScrolls!!.size - 1].firstVisiblePosition!!,
                        mViewsScrolls!![mViewsScrolls!!.size - 1].top!!)
            }

        }

        if (!mDialog.isShowing) mDialog.show()
        //else Log.d(LOG_TAG, "dialogContentView is already showing")
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
            comment.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
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