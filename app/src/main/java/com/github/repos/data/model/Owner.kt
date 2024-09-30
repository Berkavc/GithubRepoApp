package com.github.repos.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("login")
    val login: String
) : Serializable