package com.github.repos.domain.model

data class AllRepos(
    val repoName: String,
    val ownerName: String,
    val avatarUrl: String,
    val fullName: String
)