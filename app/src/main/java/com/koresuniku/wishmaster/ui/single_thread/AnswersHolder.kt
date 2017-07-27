package com.koresuniku.wishmaster.ui.single_thread

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import com.koresuniku.wishmaster.ui.anim.DialogResizeAnimation
import com.koresuniku.wishmaster.ui.anim.ResizeAnimation
import org.jetbrains.anko.find
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.opengl.ETC1.getHeight
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import org.jetbrains.anko.doAsyncResult


class AnswersHolder(val mView: AnswersHolderView) {
    val LOG_TAG: String = AnswersHolder::class.java.simpleName

    val mAnswersMap: HashMap<String, ArrayList<String>> = HashMap()
    var mPreviousSchema: IBaseJsonSchemaImpl? = null
    var mViews: ArrayList<ArrayList<View>>? = ArrayList()

    val mDialog: Dialog = Dialog(mView.getContext())
    val mStubDialog: Dialog = Dialog(mView.getContext())
    var currentPostNumber: String? = null

    val mDialogListViewContainer: FrameLayout = LayoutInflater.from(mView.getContext())
            .inflate(R.layout.dialog_list_template, null, false) as FrameLayout
    val mDialogListView: ListView = mDialogListViewContainer.find<ListView>(R.id.dialog_listview)
    var mAnswersListViewAdapter: AnswersListViewAdapter? = null

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
            Log.d(LOG_TAG, "new posts on schema: " + schema.getPosts()!!.size)
            for (post: Post in schema.getPosts()!!) {
                Log.d(LOG_TAG, "putting post: ${post.getNum()}")
                mAnswersMap.put(post.getNum(), ArrayList())
            }
        } else schema = mView.getSchema()

        for (post: Post in schema.getPosts()!!) {
            matcher = pattern.matcher(Html.fromHtml(post.getComment()))
            while (matcher.find()) {
                val group: String = matcher.group()
                Log.d(LOG_TAG, "found $group")
                if (mAnswersMap.containsKey(group.substring(2, group.length))) {
                    val answersList = mAnswersMap[group.substring(2, group.length)]
                    if (!answersList!!.contains(post.getNum())) {
                        answersList.add(post.getNum())
                        mAnswersMap.put(group.substring(2, group.length), answersList)
                    }
                }
            }
        }

        Log.d(LOG_TAG, "mAnswersMap: $mAnswersMap")

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
            spanComment(view, number)
            viewArray.add(view)
        }
        mViews!!.add(viewArray)

        openDialog()
    }

    fun openSigleAnswer(number: String) {
        currentPostNumber = number
        val viewArray: ArrayList<View> = ArrayList()
        val posts: List<Post> = mView.getSchema().getPosts()!!

        for (post in posts) {
            if (post.getNum() == number) {
                val view = mView.getDialogAdapter().getViewForDialog(
                        posts.indexOf(post), null, FrameLayout(mView.getContext()))
                viewArray.add(view)
                mViews!!.add(viewArray)
                break
            }
        }

        openDialog()
    }

    fun onBackPressed() {
        if (mViews!!.size == 1) {
            mDialog.dismiss()
            mStubDialog.dismiss()
            mViews!!.clear()
            mAnswersListViewAdapter = null
        } else {
            mViews!!.removeAt(mViews!!.size - 1)
            openDialog()
        }
    }

    fun openDialog() {
        val viewsList: List<View> = mViews!![mViews!!.size - 1]

        if (mAnswersListViewAdapter == null) {
            mAnswersListViewAdapter = AnswersListViewAdapter(viewsList)
            mDialogListView.adapter = mAnswersListViewAdapter
            mDialog.setContentView(mDialogListViewContainer)
            mDialog.window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            mDialog.setOnCancelListener({
                onBackPressed()
            })
            mStubDialog.show()
        } else {
            mDialog.dismiss()
            mAnswersListViewAdapter!!.setViewsList(viewsList)
            mDialog.show()
        }

        Log.d(LOG_TAG, "adapter count: " + mDialogListView.adapter.count)

        if (!mDialog.isShowing) mDialog.show()
        else Log.d(LOG_TAG, "dialogContentView is already showing")
    }

    fun spanComment(view: View, number: String) {
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

}