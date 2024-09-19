package com.github.repos.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllRepositories(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: Owner
): Serializable