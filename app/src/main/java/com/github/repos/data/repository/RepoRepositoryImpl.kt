package com.github.repos.data.repository

import com.github.repos.data.framework.remote.RepoService
import com.github.repos.domain.model.AllRepositories
import com.github.repos.domain.model.RepositoryDetails
import com.github.repos.domain.repository.RepoRepository
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val api: RepoService
) : RepoRepository {
    override suspend fun getAllRepositories(): List<AllRepositories> {
        return api.getAllRepositories()
    }

    override suspend fun getRepositoryDetails(
        username: String,
        repo: String
    ): RepositoryDetails {
        return api.getRepositoryDetails(username = username, repo = repo)
    }
}