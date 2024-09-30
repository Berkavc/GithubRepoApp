package com.github.repos.domain.usecase

import com.github.repos.data.model.RepositoryDetails
import com.github.repos.data.model.ResponseState
import com.github.repos.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRepositoryDetailsUseCase @Inject constructor(private val repoRepository: RepoRepository) {

    operator fun invoke(username: String, repo: String): Flow<ResponseState<RepositoryDetails>> =
        flow {
            try {
                emit(ResponseState.Loading())
                val getRepositoryDetails = repoRepository.getRepositoryDetails(username = username, repo = repo)
                emit(ResponseState.Success(getRepositoryDetails))
            } catch (e: Exception) {
                emit(ResponseState.Error(e.localizedMessage ?: "An Unexpected Error"))
            }
        }
}