package com.github.repos.domain.usecase

import com.github.repos.domain.model.AllRepositories
import com.github.repos.domain.model.ResponseState
import com.github.repos.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
class MainUseCase @Inject constructor(private val repoRepository: RepoRepository) {
    /** [GetAllRepositoriesUseCase]in aynisi */
    operator fun invoke(): Flow<ResponseState<List<AllRepositories>>> = flow {
        try {
            emit(ResponseState.Loading())
            val getAllRepos = repoRepository.getAllRepositories()
            emit(ResponseState.Success(getAllRepos))

        } catch (e: Exception) {
            emit(ResponseState.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }
}