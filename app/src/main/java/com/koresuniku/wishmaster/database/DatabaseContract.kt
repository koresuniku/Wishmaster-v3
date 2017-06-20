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
        val COLUMN_BOARD_CATEGORY = "board_category"
        val COLUMN_BOARD_PREFERRED = "board_preferred"

        val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME)!!

        val BOARD_PREFERRED_FALSE: Int = 0
        val BOARD_PREFERRED_TRUE: Int = 1
    }

}