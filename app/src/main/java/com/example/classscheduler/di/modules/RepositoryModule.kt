package com.example.classscheduler.di.modules

import com.example.classscheduler.data.datasource.AuthRemoteDataSource
import com.example.classscheduler.data.datasource.ClassRemoteDataSource
import com.example.classscheduler.data.repository.AuthRepositoryImpl
import com.example.classscheduler.data.repository.ClassRepositoryImpl
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.domain.interfaces.ClassRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun authRepository(
        authRemoteDataSource: AuthRemoteDataSource
    ): AuthRepository{
        return AuthRepositoryImpl(authRemoteDataSource);
    }

    @Provides
    fun classRepository(
        clasRemoteDataSource: ClassRemoteDataSource
    ): ClassRepository{
        return ClassRepositoryImpl(clasRemoteDataSource);
    }
}