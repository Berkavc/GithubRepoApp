package com.github.repos.domain.repository

import com.github.repos.data.dto.allReposDto.AllRepositoriesDto
import com.github.repos.data.dto.repoDetailsDto.RepositoryDetailsDto

interface RepoRepository {
    suspend fun getAllRepositories(): AllRepositoriesDto
    suspend fun getRepositoryDetails(username: String, repo: String): RepositoryDetailsDto
}