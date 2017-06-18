package com.koresuniku.wishmaster.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_BOARDS_TABLE = "CREATE TABLE " + DatabaseContract.BoardsEntry.TABLE_NAME
        CREATE_BOARDS_TABLE.plus(" (" + DatabaseContract.BoardsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ")
        CREATE_BOARDS_TABLE.plus(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " TEXT NOT NULL, ")
        CREATE_BOARDS_TABLE.plus(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME + " TEXT NOT NULL, ")
        CREATE_BOARDS_TABLE.plus(DatabaseContract.BoardsEntry.COLUMN_BOARD_IF_PREFERRED_POSITION + " INTEGER NOT NULL DEFAULT -1);")

        db.execSQL(CREATE_BOARDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val DATABASE_NAME = "wishmaster.db"
        private val DATABASE_VERSION = 1
    }
}
