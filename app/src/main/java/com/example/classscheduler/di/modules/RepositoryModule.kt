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

/**
 * Hilt module responsible for providing repository implementations
 * used throughout the application.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides the implementation of [AuthRepository], backed by
     * [AuthRemoteDataSource].
     *
     * @param authRemoteDataSource The remote data source handling Firebase authentication.
     * @return The concrete [AuthRepository] implementation.
     */
    @Provides
    fun authRepository(
        authRemoteDataSource: AuthRemoteDataSource
    ): AuthRepository{
        return AuthRepositoryImpl(authRemoteDataSource);
    }


    /**
     * Provides the implementation of [ClassRepository], backed by
     * [ClassRemoteDataSource].
     *
     * @param clasRemoteDataSource The remote data source responsible for class-related Firestore operations.
     * @return The concrete [ClassRepository] implementation.
     */
    @Provides
    fun classRepository(
        clasRemoteDataSource: ClassRemoteDataSource
    ): ClassRepository{
        return ClassRepositoryImpl(clasRemoteDataSource);
    }
}