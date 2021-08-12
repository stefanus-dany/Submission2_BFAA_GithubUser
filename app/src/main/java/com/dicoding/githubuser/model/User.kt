package com.dicoding.githubuser.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var img: String? = "",
    var name: String? = "",
    var username: String? = "",
    var following: String? = "",
    var followers: String? = "",
    var company: String? = "",
    var location: String? = "",
    var repository: String? = ""
) : Parcelable