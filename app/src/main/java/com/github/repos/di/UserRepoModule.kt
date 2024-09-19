package com.github.repos.di

import com.github.repos.data.framework.remote.RepoService
import com.github.repos.data.repository.RepoRepositoryImpl
import com.github.repos.domain.repository.RepoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepoModule {

    @Provides
    @Singleton
    fun provideReposApi(): RepoService {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30L, TimeUnit.SECONDS)
            .connectTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RepoService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: RepoService): RepoRepository {
        return RepoRepositoryImpl(api = api)
    }
}