package com.github.repos.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RepositoryDetails(
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("language")
    val language: String,
    @SerializedName("defaultBranch")
    val defaultBranch: String,
    @SerializedName("forks_count")
    val forksCount: Int
): Serializable