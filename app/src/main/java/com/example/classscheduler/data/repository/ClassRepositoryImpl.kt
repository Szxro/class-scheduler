package com.example.classscheduler.data.repository

import com.example.classscheduler.data.datasource.ClassRemoteDataSource
import com.example.classscheduler.domain.interfaces.ClassRepository
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.primitives.Result
import javax.inject.Inject

/**
 * Implementation of [ClassRepository] that delegates all class-related
 * operations to the remote Firestore-based data source [ClassRemoteDataSource].
 *
 * @property classRemoteDataSource The underlying data source that handles
 * Firestore interactions for class management.
 */
class ClassRepositoryImpl @Inject constructor(
    private val classRemoteDataSource: ClassRemoteDataSource
) : ClassRepository {

    /**
     * Creates a new class entry in the remote data source.
     *
     * @param newClass The class information to be persisted.
     * @return A [Result] indicating success or failure of the operation.
     */
    override suspend fun create(newClass: Class): Result<Nothing> {
        return classRemoteDataSource.create(newClass);
    }

    /**
     * Updates an existing class in the remote data source.
     *
     * @param updatedClass The modified class data, including its ID.
     * @return A [Result] representing the outcome of the update operation.
     */
    override suspend fun update(updatedClass: Class): Result<Nothing> {
        return classRemoteDataSource.update(updatedClass);
    }

    /**
     * Deletes a class from the remote data source.
     *
     * @param classId The unique identifier of the class to delete.
     * @return A [Result] reflecting whether the deletion was successful.
     */
    override suspend fun delete(classId: String): Result<Nothing> {
        return classRemoteDataSource.delete(classId);
    }

    /**
     * Retrieves all classes for a given owner that occur on the specified day.
     *
     * @param day The schedule day to filter by.
     * @param ownerId The ID of the user who owns the classes.
     * @return A [Result] containing a list of matching classes or an error.
     */
    override suspend fun getClassesByDay(day: String, ownerId: String): Result<List<Class>> {
        return classRemoteDataSource.getClassesByDay(day, ownerId);
    }

    /**
     * Retrieves all classes associated with a specific owner.
     *
     * @param ownerId The ID of the user who owns the classes.
     * @return A [Result] with all classes belonging to the owner.
     */
    override suspend fun getClassesByOwnerId(ownerId: String): Result<List<Class>> {
        return classRemoteDataSource.getClassesByOwnerId(ownerId);
    }

    /**
     * Retrieves all classes owned by a user that have not been configured yet.
     * A class is considered unconfigured when its `configured` field is false.
     *
     * @param ownerId The ID of the class owner.
     * @return A [Result] with the unconfigured classes or an error.
     */
    override suspend fun getUnconfiguredClassesByOwnerId(ownerId: String): Result<List<Class>> {
        return classRemoteDataSource.getUnconfiguredClassesByOwnerId(ownerId);
    }
}