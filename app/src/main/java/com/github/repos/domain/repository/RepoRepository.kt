package com.github.repos.domain.repository

import com.github.repos.data.model.AllRepositories
import com.github.repos.data.model.RepositoryDetails

interface RepoRepository {
    suspend fun getAllRepositories(): List<AllRepositories>
    suspend fun getRepositoryDetails(username: String, repo: String): RepositoryDetails
}