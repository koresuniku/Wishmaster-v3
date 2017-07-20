package com.koresuniku.wishmaster.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class BoardsProvider : ContentProvider() {
    val LOG_TAG: String = BoardsProvider::class.java.simpleName

    var mDatabaseHelper: DatabaseHelper? = null

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val database: SQLiteDatabase = mDatabaseHelper!!.writableDatabase

        val id = database.insert(DatabaseContract.BoardsEntry.TABLE_NAME, null, values)

        return ContentUris.withAppendedId(uri, id)
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val database: SQLiteDatabase = mDatabaseHelper!!.readableDatabase

        val cursor: Cursor = database.query(DatabaseContract.BoardsEntry.TABLE_NAME, projection,
                selection, selectionArgs, null, null, null)

        return cursor
    }

    override fun onCreate(): Boolean {
        mDatabaseHelper = DatabaseHelper(context)

        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val database: SQLiteDatabase = mDatabaseHelper!!.writableDatabase

        val number: Int = database.update(DatabaseContract.BoardsEntry.TABLE_NAME, values, selection, selectionArgs)

        return number
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val database: SQLiteDatabase = mDatabaseHelper!!.writableDatabase
        val number: Int = database.delete(DatabaseContract.BoardsEntry.TABLE_NAME, selection, selectionArgs)
        return number
    }

    override fun getType(uri: Uri?): String {
        return ""
    }


}