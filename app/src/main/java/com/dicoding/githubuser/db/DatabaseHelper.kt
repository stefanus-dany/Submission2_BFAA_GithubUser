package com.dicoding.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbfavuser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseUser.UserColumns._USERNAME} TEXT NOT NULL PRIMARY KEY," +
                " ${DatabaseUser.UserColumns.IMG} TEXT NOT NULL," +
                " ${DatabaseUser.UserColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseUser.UserColumns.FOLLOWING} TEXT NOT NULL," +
                " ${DatabaseUser.UserColumns.FOLLOWERS} TEXT NOT NULL," +
                " ${DatabaseUser.UserColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseUser.UserColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseUser.UserColumns.REPOSITORY} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}