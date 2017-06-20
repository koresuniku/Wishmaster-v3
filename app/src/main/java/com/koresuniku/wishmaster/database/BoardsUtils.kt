package com.koresuniku.wishmaster.database

import android.app.Activity
import android.database.Cursor
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.boards_api.BoardsJsonSchema
import com.koresuniku.wishmaster.http.boards_api.model.*
import java.util.ArrayList

object BoardsUtils {
    val mBoardsProjection = arrayOf(
            DatabaseContract.BoardsEntry.COLUMN_BOARD_ID,
            DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME,
            DatabaseContract.BoardsEntry.COLUMN_BOARD_PREFERRED)

    fun getSchema(mActivity: Activity): BoardsJsonSchema {
        var mSchema: BoardsJsonSchema = BoardsJsonSchema()

        var cursor: Cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection, null, null, null)

        val columnBoardId = cursor.getColumnIndex(
                DatabaseContract.BoardsEntry.COLUMN_BOARD_ID)
        val columnBoardName = cursor.getColumnIndex(
                DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME)
        val columnBoardPreferredPosition = cursor.getColumnIndex(
                DatabaseContract.BoardsEntry.COLUMN_BOARD_PREFERRED)

        var boardId: String
        var boardName: String
        var boardPreferredPosition: Int

        val adultsList = ArrayList<Adults>()
        var adults: Adults
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.adult_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            adults = Adults()
            adults.id = boardId
            adults.name = boardName
            adults.isPreferred = boardPreferredPosition >= 0
            adultsList.add(adults)
            cursor.moveToNext()
        }
        mSchema.adults = adultsList

        val creativityList = ArrayList<Creativity>()
        var creativity: Creativity
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.creativity_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            creativity = Creativity()
            creativity.id = boardId
            creativity.name = boardName
            creativity.isPreferred = boardPreferredPosition >= 0
            creativityList.add(creativity)

            cursor.moveToNext()
        }
        mSchema.creativity = creativityList

        val gamesList = ArrayList<Games>()
        var games: Games
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.games_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            games = Games()
            games.id = boardId
            games.name = boardName
            games.isPreferred = boardPreferredPosition >= 0
            gamesList.add(games)

            cursor.moveToNext()
        }
        mSchema.games = gamesList


        val japaneseList = ArrayList<Japanese>()
        var japanese: Japanese
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.japanese_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            japanese = Japanese()
            japanese.id = boardId
            japanese.name = boardName
            japanese.isPreferred = boardPreferredPosition >= 0
            japaneseList.add(japanese)

            cursor.moveToNext()
        }
        mSchema.japanese = japaneseList


        val otherList = ArrayList<Other>()
        var other: Other
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.different_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            other = Other()
            other.id = boardId
            other.name = boardName
            other.isPreferred = boardPreferredPosition >= 0
            otherList.add(other)

            cursor.moveToNext()
        }
        mSchema.other = otherList


        val politicsList = ArrayList<Politics>()
        var politics: Politics
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.politics_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            politics = Politics()
            politics.id = boardId
            politics.name = boardName
            politics.isPreferred = boardPreferredPosition >= 0
            politicsList.add(politics)

            cursor.moveToNext()
        }
        mSchema.politics = politicsList


        val subjectsList = ArrayList<Subjects>()
        var subjects: Subjects
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.subject_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            subjects = Subjects()
            subjects.id = boardId
            subjects.name = boardName
            subjects.isPreferred = boardPreferredPosition >= 0
            subjectsList.add(subjects)

            cursor.moveToNext()
        }
        mSchema.subject = subjectsList


        val techList = ArrayList<Tech>()
        var tech: Tech
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.tech_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            tech = Tech()
            tech.id = boardId
            tech.name = boardName
            tech.isPreferred = boardPreferredPosition >= 0
            techList.add(tech)

            cursor.moveToNext()
        }
        mSchema.tech = techList


        val usersList = ArrayList<Users>()
        var users: Users
        cursor = mActivity.contentResolver.query(
                DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection,
                DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " = " + R.string.users_boards_en, null, null)
        cursor.moveToFirst()
        for (i in 0..cursor.count - 1) {
            boardId = cursor.getString(columnBoardId)
            boardName = cursor.getString(columnBoardName)
            boardPreferredPosition = cursor.getInt(columnBoardPreferredPosition)
            users = Users()
            users.id = boardId
            users.name = boardName
            users.isPreferred = boardPreferredPosition >= 0
            usersList.add(users)

            cursor.moveToNext()
        }
        mSchema.users = usersList


        cursor.close()

        return mSchema
    }

    fun queryABoard(activity: Activity, boardId: String?): Cursor {

        val cursor: Cursor = activity.contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection, DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                arrayOf(boardId), null, null)

        return cursor
    }
}