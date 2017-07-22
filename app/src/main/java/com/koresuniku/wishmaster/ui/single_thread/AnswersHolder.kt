package com.koresuniku.wishmaster.ui.single_thread

import android.text.Html
import android.util.Log
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.single_thread_api.model.Post
import java.util.regex.Matcher
import java.util.regex.Pattern

class AnswersHolder(val mView: AnswersHolderView) {
    val LOG_TAG: String = AnswersHolder::class.java.simpleName

    val mAnswersMap: HashMap<String, ArrayList<String>> = HashMap()
    var mPreviousSchema: IBaseJsonSchemaImpl? = null

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
                Log.d(LOG_TAG, "found >> $group")
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
}