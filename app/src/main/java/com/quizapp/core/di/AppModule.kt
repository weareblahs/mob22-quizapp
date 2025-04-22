package com.quizapp.core.di

import com.quizapp.core.service.AuthService
import com.quizapp.core.service.AuthServiceImpl
import com.quizapp.data.repo.QuestionRepo
import com.quizapp.data.repo.QuestionRepoImpl
import com.quizapp.data.repo.QuizRepo
import com.quizapp.data.repo.QuizRepoImpl
import com.quizapp.data.repo.UserRepo
import com.quizapp.data.repo.UserRepoImpl
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

    @Provides
    @Singleton
    fun provideUserRepo(authService: AuthService): UserRepo {
        return UserRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun provideQuestionRepo(authService: AuthService): QuestionRepo {
        return QuestionRepoImpl(authService = authService)
    }
}