package com.hamaro.rockettranslatenativeapp.data.remote.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.domain.ImageRepository
import com.hamaro.rockettranslatenativeapp.data.remote.model.ImageRequest
import com.hamaro.rockettranslatenativeapp.domain.model.ImageFirestore
import com.hamaro.rockettranslatenativeapp.domain.model.ImageHistory
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ImageRepositoryImpl(
    private val authService : AuthService
) : ImageRepository {

    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val usersCollection = "users"
        private const val imagesCollection = "image_captures"
    }

    override suspend fun saveImageRequest(imageRequest: ImageRequest): RequestState<String> {
        return try {
            val userId = authService.currentUserId
            val userDocRef = firestore.collection(usersCollection).document(userId)
            val imageCollectionRef = userDocRef.collection(imagesCollection)

            val newImage = hashMapOf(
                "base64" to imageRequest.base64,
                "date_time_captured" to imageRequest.dateTimeCaptured
            )

            userDocRef.get().addOnCompleteListener { task ->
                if (!task.result.exists()) {
                    userDocRef.set(hashMapOf<String, String>())
                }
            }

            imageCollectionRef.add(newImage).await()
            RequestState.Success("Image saved successfully!")
        } catch (e: Exception) {
            RequestState.Error(e.message ?: "Failed to save image")
        }
    }

    override suspend fun getAllImages(): Flow<RequestState<ImageHistory>> {
        return flow {
            emit(RequestState.Loading)
            try {
                val userId = getCurrentUserId()
                val userDocRef = firestore.collection(usersCollection).document(userId)
                val imageCollectionRef = userDocRef.collection(imagesCollection)

                val snapshot = imageCollectionRef.get().await()

                if (snapshot.isEmpty) {
                    emit(RequestState.Error("No images found for the user."))
                } else {
                    val images = snapshot.documents.map { doc ->
                        ImageFirestore(
                            imageBaseEncoded = doc.getString("base64") ?: "",
                            createdAt = doc.getString("date_time_captured") ?: ""
                        )
                    }
                    emit(RequestState.Success(ImageHistory(images)))
                }
            } catch (e: Exception) {
                emit(RequestState.Error(e.message ?: "Failed to retrieve images"))
            }
        }
    }

    private fun getCurrentUserId() : String {
        return authService.currentUserId
    }
}