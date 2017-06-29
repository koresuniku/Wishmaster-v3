package com.koresuniku.wishmaster.ui.text

import android.content.Context
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.thread_list_api.model.Files

object TextUtils {

    fun getPostsAndFilesString(postCount: Int, filesCount: Int): String {
        var result: String = ""
        result += getCorrectRussianEndings(
                count = postCount, stringForZeroOrMultiple = "постов",
                stringForOne = "пост", stringForTwoOrThreeOrFour = "поста")
        result += ", "
        result += getCorrectRussianEndings(
                count = filesCount, stringForZeroOrMultiple = "файлов",
                stringForOne = "файл", stringForTwoOrThreeOrFour = "файла")
        return result
    }

    fun getCorrectRussianEndings(count: Int, stringForZeroOrMultiple: String,
                                 stringForOne: String, stringForTwoOrThreeOrFour: String): String {
        if (count == 0) return count.toString() + " " + stringForZeroOrMultiple
        val lastNumber: Int
        var signsCount = -1
        if (count < 10) {
            lastNumber = count
        } else {
            signsCount = count.toString().length
            lastNumber = Integer.parseInt(count.toString().substring(signsCount - 1, signsCount))
        }
        if (count in 10..20) {
            return count.toString() + " " +  stringForZeroOrMultiple
        }
        if (lastNumber == 1) {
            if (count >= 10 && count.toString().substring(signsCount - 1, signsCount) == "11") {
                return count.toString() + " " + stringForZeroOrMultiple
            }
            return count.toString() + " " + stringForOne
        }
        if (lastNumber == 2 || lastNumber == 3 || lastNumber == 4) {
            return count.toString() + " " + stringForTwoOrThreeOrFour
        }
        return count.toString() + " " + stringForZeroOrMultiple
    }

    fun getSummaryString(context: Context, file: Files): String {
        val size = file.getSize()
        val width = file.getWidth()
        val height = file.getHeight()
        val builder = StringBuilder()

        builder.append(size)
        builder.append("Кб")
        builder.append(", ")
        builder.append(width)
        builder.append("x")
        builder.append(height)

        return builder.toString()
    }
}