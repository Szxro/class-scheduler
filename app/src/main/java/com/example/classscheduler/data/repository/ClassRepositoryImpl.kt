package com.example.classscheduler.data.repository

import com.example.classscheduler.data.datasource.ClassRemoteDataSource
import com.example.classscheduler.domain.interfaces.ClassRepository
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.primitives.Result
import javax.inject.Inject

class ClassRepositoryImpl @Inject constructor(
    private val classRemoteDataSource: ClassRemoteDataSource
) : ClassRepository {

    override suspend fun create(newClass: Class): Result<Nothing> {
        return classRemoteDataSource.create(newClass);
    }

    override suspend fun update(updatedClass: Class): Result<Nothing> {
        return classRemoteDataSource.update(updatedClass);
    }

    override suspend fun delete(classId: String): Result<Nothing> {
        return classRemoteDataSource.delete(classId);
    }

    override suspend fun getClassesByDay(day: String, ownerId: String): Result<List<Class>> {
        return classRemoteDataSource.getClassesByDay(day, ownerId);
    }

    override suspend fun getClassesByOwnerId(ownerId: String): Result<List<Class>> {
        return classRemoteDataSource.getClassesByOwnerId(ownerId);
    }

    override suspend fun getUnconfiguredClassesByOwnerId(ownerId: String): Result<List<Class>> {
        return classRemoteDataSource.getUnconfiguredClassesByOwnerId(ownerId);
    }
}