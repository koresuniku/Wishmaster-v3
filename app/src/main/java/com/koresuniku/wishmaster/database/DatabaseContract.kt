package com.koresuniku.wishmaster.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    val CONTENT_AUTHORITY = "com.koresuniku.wishmaster"
    val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)!!

    object BoardsEntry : BaseColumns {
        val TABLE_NAME = "boards"

        val COLUMN_ID = "_id"
        val COLUMN_BOARD_ID = "board_id"
        val COLUMN_BOARD_NAME = "board_name"
        val COLUMN_BOARD_IF_PREFERRED_POSITION = "board_if_preferred_position"

        val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME)
    }
}