package com.github.repos.domain.repository

import com.github.repos.domain.model.AllRepositories
import com.github.repos.domain.model.RepositoryDetails

interface RepoRepository {
    suspend fun getAllRepositories(): List<AllRepositories>
    suspend fun getRepositoryDetails(username: String, repo: String): RepositoryDetails
}