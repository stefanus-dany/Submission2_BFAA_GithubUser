package com.dicoding.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion.TABLE_NAME
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion._USERNAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    //get all data
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_USERNAME ASC")
    }

    //get data with username
    fun queryByUsername(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    //insert data
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    //update data
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_USERNAME = ?", arrayOf(id))
    }

    //delete data
    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$_USERNAME = '$username'", null)
    }

}