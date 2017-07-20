package com.koresuniku.wishmaster.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_BOARDS_TABLE: StringBuilder  = StringBuilder("CREATE TABLE " + DatabaseContract.BoardsEntry.TABLE_NAME)
        CREATE_BOARDS_TABLE.append(" (" + DatabaseContract.BoardsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ")
        CREATE_BOARDS_TABLE.append(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " TEXT NOT NULL, ")
        CREATE_BOARDS_TABLE.append(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME + " TEXT NOT NULL, ")
        CREATE_BOARDS_TABLE.append(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY + " TEXT NOT NULL, ")
        CREATE_BOARDS_TABLE.append(DatabaseContract.BoardsEntry.COLUMN_BOARD_PREFERRED + " INTEGER NOT NULL DEFAULT "
                + DatabaseContract.BoardsEntry.BOARD_PREFERRED_FALSE + ");")

        Log.d("DBhelper", "Full string: " + CREATE_BOARDS_TABLE)

        db.execSQL(CREATE_BOARDS_TABLE.toString())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val DATABASE_NAME = "wishmaster.db"
        private val DATABASE_VERSION = 1
    }
}
