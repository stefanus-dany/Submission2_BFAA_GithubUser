package com.dicoding.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseUser {

    const val AUTHORITY = "com.dicoding.githubuser"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val IMG = "img"
            const val NAME = "name"
            const val _USERNAME = "_username"
            const val FOLLOWING = "following"
            const val FOLLOWERS = "followers"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"

            // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/note
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}