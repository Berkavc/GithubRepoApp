package com.github.repos.data.framework.remote

import com.github.repos.data.dto.allReposDto.AllRepositoriesDto
import com.github.repos.data.dto.repoDetailsDto.RepositoryDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoService {

    @GET("repositories")
    suspend fun getAllRepositories(): AllRepositoriesDto

    @GET("repos/{username}/{repo}")
    suspend fun getRepositoryDetails(@Path("username")username:String,@Path("repo")repo:String): RepositoryDetailsDto
}