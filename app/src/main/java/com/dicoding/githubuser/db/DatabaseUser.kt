package com.dicoding.githubuser.db

import android.provider.BaseColumns

internal class DatabaseUser {

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
        }
    }
}