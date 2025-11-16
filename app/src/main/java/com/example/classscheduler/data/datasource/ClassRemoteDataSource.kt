package com.example.classscheduler.data.datasource

import android.util.Log
import com.example.classscheduler.core.common.BaseDataSource
import com.example.classscheduler.domain.errors.GenericError
import com.example.classscheduler.domain.models.Class
import com.example.classscheduler.domain.primitives.Error
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.classscheduler.domain.primitives.Result;
import com.google.firebase.firestore.toObjects
import javax.inject.Inject

class ClassRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
): BaseDataSource("ClassRemoteDataSource") {

    suspend fun create(newClass: Class): Result<Nothing> {
        return try {
            firestore
                .collection(CLASSES_COLLECTION)
                .add(newClass).
                await();

            Log.d(TAG, "create:success")

            Result.onSuccess();
        } catch (exception: Exception) {
            Log.w(TAG,"create:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    suspend fun update(updatedClass: Class):Result<Nothing>{
        return try {
            firestore
                .collection(CLASSES_COLLECTION)
                .document(updatedClass.id)
                .set(updatedClass)
                .await();

            Log.d(TAG, "update:success");

            Result.onSuccess();
        }catch (exception: Exception){
            Log.w(TAG,"update:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    suspend fun delete(classId: String): Result<Nothing> {
        return try {
            firestore
                .collection(CLASSES_COLLECTION)
                .document(classId)
                .delete()
                .await();

            Log.d(TAG, "delete:success");

            Result.onSuccess();
        } catch (exception: Exception) {
            Log.w(TAG,"delete:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    suspend fun getClassesByDay(day: String, ownerId:String): Result<List<Class>> {
        return try {
            val classes = firestore.collection(CLASSES_COLLECTION)
                         .whereEqualTo("ownerId", ownerId)
                         .whereArrayContains("scheduleDays", day)
                         .get()
                         .await()
                         .toObjects<Class>();

            Log.d(TAG, "getClassesByDay:success");

            Result.onSuccess(classes);
        } catch (exception: Exception) {
            Log.w(TAG,"getClassesByDay:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    suspend fun getClassesByOwnerId(ownerId: String): Result<List<Class>>{
        return try {
            val classes = firestore.collection(CLASSES_COLLECTION)
                                    .whereEqualTo("ownerId", ownerId)
                                    .get()
                                    .await()
                                    .toObjects<Class>();

            Log.d(TAG, "getClassesByOwnerId:success");

            Result.onSuccess(classes);
        }catch (exception: Exception){
            Log.w(TAG,"getClassesByOwnerId:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    suspend fun getUnconfiguredClassesByOwnerId(ownerId: String):Result<List<Class>>{
        return  try {
            val classes = firestore.collection(CLASSES_COLLECTION)
                                    .whereEqualTo("ownerId", ownerId)
                                    .whereEqualTo("configured", false)
                                    .get()
                                    .await()
                                    .toObjects<Class>();

            Log.d(TAG, "getUnconfiguredClassesByOwnerId:success");

            Result.onSuccess(classes);
        }catch (exception: Exception){
            Log.w(TAG,"getUnconfiguredClassesByOwnerId:failure", exception);

            val error = getErrorFromException(exception);

            Result.onFailure(error);
        }
    }

    override fun getErrorFromException(exception: Exception): Error {
        val error = when(exception){
            else -> GenericError.UnknownError
        }

        return error;
    }

    companion object {
        private const val CLASSES_COLLECTION = "classes";
    }
}