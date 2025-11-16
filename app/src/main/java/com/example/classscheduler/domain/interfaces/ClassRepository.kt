package com.example.classscheduler.domain.interfaces

import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.primitives.Result

interface ClassRepository {

    suspend fun create(newClass: Class): Result<Nothing>;

    suspend fun delete(classId: String): Result<Nothing>;

    suspend fun update(updatedClass: Class):Result<Nothing>;

    suspend fun getClassesByDay(day: String, ownerId: String): Result<List<Class>>;


    suspend fun getClassesByOwnerId(ownerId: String): Result<List<Class>>;


    suspend fun getUnconfiguredClassesByOwnerId(ownerId: String):Result<List<Class>>;
}