package com.koresuniku.wishmaster.ui.single_thread

import android.widget.Toast
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.text.TextUtils

class NewPostsNotifier(val mView: NewPostsView) {
    val LOG_TAG: String = NewPostsNotifier::class.java.simpleName


    var previousPostsCount: Int = 0

    fun fetchPostsCount(postsCount: Int) {
        previousPostsCount = postsCount
    }

    fun notifyNewPosts(newPostsCount: Int) {
        val newPostsDifference = newPostsCount - previousPostsCount
        var toastString: String = ""

        if (newPostsDifference != 0) {
            toastString += TextUtils.getCorrectRussianEndings(newPostsDifference,
                    stringForZeroOrMultiple = "новых постов", stringForOne = "новый пост",
                    stringForTwoOrThreeOrFour = "новых поста")
            val toast = Toast.makeText(mView.getContext(), toastString, Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}