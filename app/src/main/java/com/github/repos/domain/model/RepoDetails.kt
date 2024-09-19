package com.github.repos.domain.model

data class RepoDetails(
    val repoName: String,
    val ownerName: String,
    val avatar: String,
    val forksCount: Int,
    val language: String?,
    val default_branch: String
)