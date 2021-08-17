package com.dicoding.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.githubuser.db.DatabaseUser.AUTHORITY
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion.TABLE_NAME
import com.dicoding.githubuser.db.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val NOTE = 1
        private const val NOTE_ID = 2
        private lateinit var favHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", NOTE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavoriteHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            NOTE -> favHelper.queryAll()
            NOTE_ID -> favHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (NOTE) {
            sUriMatcher.match(uri) -> favHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        val updated: Int = when (NOTE_ID) {
            sUriMatcher.match(uri) -> favHelper.update(uri.lastPathSegment.toString(),contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (NOTE_ID) {
            sUriMatcher.match(uri) -> favHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

}