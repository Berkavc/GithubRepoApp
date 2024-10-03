package com.github.repos.data.framework.remote

import com.github.repos.data.model.AllRepositories
import com.github.repos.data.model.RepositoryDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoService {

    @GET("repositories")
    suspend fun getAllRepositories(): List<AllRepositories>

    @GET("repos/{username}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("username") username: String,
        @Path("repo") repo: String
    ): RepositoryDetails
}