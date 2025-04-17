package com.quizapp.core.di

import com.quizapp.core.service.AuthService
import com.quizapp.core.service.AuthServiceImpl
import com.quizapp.data.repo.QuizRepo
import com.quizapp.data.repo.QuizRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }

    @Provides
    @Singleton
    fun provideQuizRepo(authService: AuthService): QuizRepo {
        return QuizRepoImpl(authService = authService)
    }
}