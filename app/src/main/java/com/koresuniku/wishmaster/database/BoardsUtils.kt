package com.koresuniku.wishmaster.database

import android.app.Activity
import android.content.ContentValues
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

    fun writeInAllTheBoardsIntoDatabase(mSchema: BoardsJsonSchema?, activity: Activity) {
        var values: ContentValues = ContentValues()

        for (board in mSchema!!.adults!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_ADULTS)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.creativity!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_CREATIVITY)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.games!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_GAMES)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.japanese!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_JAPANESE)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.other!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_OTHER)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.politics!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_POLITICS)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.subject!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_SUBJECTS)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.tech!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_TECH)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }

        for (board in mSchema!!.users!!) {
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
            values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, DatabaseContract.BoardsEntry.CATEGORY_USERS)
            activity.contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
            values = ContentValues()
        }
    }

    fun insertNewBoards(mSchema: BoardsJsonSchema?, activity: Activity) {
        val boardsNewIdsSet: Set<String> = HashSet()

        for (adults: Adults in mSchema!!.adults!!) {
            boardsNewIdsSet.plus(adults.id)
        }
        for (creativity: Creativity in mSchema!!.creativity!!) {
            boardsNewIdsSet.plus(creativity.id)
        }
        for (games: Games in mSchema!!.games!!) {
            boardsNewIdsSet.plus(games.id)
        }
        for (japanese: Japanese in mSchema!!.japanese!!) {
            boardsNewIdsSet.plus(japanese.id)
        }
        for (other: Other in mSchema!!.other!!) {
            boardsNewIdsSet.plus(other.id)
        }
        for (politics: Politics in mSchema!!.politics!!) {
            boardsNewIdsSet.plus(politics.id)
        }
        for (subjects: Subjects in mSchema!!.subject!!) {
            boardsNewIdsSet.plus(subjects.id)
        }
        for (tech: Tech in mSchema!!.tech!!) {
            boardsNewIdsSet.plus(tech.id)
        }
        for (users: Users in mSchema!!.users!!) {
            boardsNewIdsSet.plus(users.id)
        }

        val cursor: Cursor = activity.contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI,
                mBoardsProjection, null, null, null, null)
        val columnIndex = cursor.getColumnIndex(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID)
        var boardId: String
        val boardsDatabaseIdsSet: Set<String> = HashSet()
        while (cursor.moveToNext()) {
            boardId = cursor.getString(columnIndex)
            boardsDatabaseIdsSet.plus(boardId)
        }
        cursor.close()

        val resultSet: Set<String> = boardsNewIdsSet.subtract(boardsDatabaseIdsSet)

        for (newBoardId: String in resultSet) {
            mSchema!!.adults!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_ADULTS))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.creativity!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_CREATIVITY))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.games!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_GAMES))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.japanese!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_JAPANESE))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.other!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_OTHER))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.politics!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_POLITICS))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.subject!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_SUBJECTS))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.tech!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_TECH))
            }
        }
        for (newBoardId: String in resultSet) {
            mSchema!!.users!!.filter { it.id!! == newBoardId }.forEach {
                insertNewBoard(arrayOf(it.id, it.name, DatabaseContract.BoardsEntry.CATEGORY_USERS))
            }
        }
    }

    fun insertNewBoard(boardData: Array<String?>) {
        val values: ContentValues = ContentValues();
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, boardData[0])
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, boardData[1])
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, boardData[2])

    }
}