package com.dicoding.githubuser.helper

import android.database.Cursor
import com.dicoding.githubuser.db.DatabaseUser
import com.dicoding.githubuser.model.User

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val notesList = ArrayList<User>()
        notesCursor?.apply {
            while (moveToNext()) {
                val img = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.IMG))
                val name = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns._USERNAME))
                val following = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.FOLLOWING))
                val followers = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.FOLLOWERS))
                val company = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.LOCATION))
                val repository =
                    getString(getColumnIndexOrThrow(DatabaseUser.UserColumns.REPOSITORY))
                notesList.add(
                    User(
                        img = img,
                        name = name,
                        username = username,
                        following = following,
                        followers = followers,
                        company = company,
                        location = location,
                        repository = repository
                    )
                )
            }
        }
        return notesList
    }
}