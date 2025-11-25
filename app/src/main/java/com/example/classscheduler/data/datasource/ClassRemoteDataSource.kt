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

/**
 * Remote data source responsible for interacting with the Firestore database
 * to perform CRUD operations on [Class] entity.
 *
 * @property firestore The [FirebaseFirestore] instance used for database operations.
 */
class ClassRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
): BaseDataSource("ClassRemoteDataSource") {

    /**
     * Creates a new class document in Firestore.
     *
     * @param newClass The class entity to be created.
     * @return [Result.Success] if the class is successfully created,
     *         or [Result.Failure] if any Firestore error occurs.
     */
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

    /**
     * Updates an existing class document in Firestore.
     *
     * The class is identified by its `id` property.
     *
     * @param updatedClass The updated class data to be stored.
     * @return A [Result] representing either success or failure.
     */
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

    /**
     * Deletes a class document from Firestore.
     *
     * @param classId The ID of the class to delete.
     * @return A [Result] indicating whether the deletion succeeded or failed.
     */
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

    /**
     * Retrieves all classes that:
     *  - Belong to a given owner, and
     *  - Contain the specified day in their `scheduleDays` array.
     *
     * @param day The day to match in the schedule.
     * @param ownerId The ID of the user who owns the classes.
     * @return A [Result] containing a list of classes or a failure error.
     */
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

    /**
     * Retrieves all classes owned by the specified user.
     *
     * @param ownerId The ID of the user who owns the classes.
     * @return A [Result] containing all classes for this owner.
     */
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

    /**
     * Retrieves all classes owned by a user that have *not* been configured yet.
     * A class is considered unconfigured when its `configured` field is false.
     *
     * @param ownerId The ID of the class owner.
     * @return A [Result] containing a list of unconfigured classes.
     */
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