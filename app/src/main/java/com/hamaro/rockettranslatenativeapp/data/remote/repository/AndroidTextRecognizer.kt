package com.hamaro.rockettranslatenativeapp.data.remote.repository

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import com.hamaro.rockettranslatenativeapp.domain.TextRecognizer
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AndroidTextRecognizer(
    private val functions: FirebaseFunctions
) : TextRecognizer {

    override suspend fun recognizeText(base64encoded: String): RequestState<String> {
        Log.d("Debug", "Starting text recognition")
        Log.d("Debug", "Base64 input: $base64encoded")

        return try {
            val request = JsonObject().apply {
                add("image", JsonObject().apply {
                    add("content", JsonPrimitive(base64encoded))
                })
                add("features", JsonArray().apply {
                    add(JsonObject().apply {
                        add("type", JsonPrimitive("TEXT_DETECTION"))
                    })
                })
            }

            Log.d("Debug", "Request JSON: ${request.toString()}")

            val annotation = annotateImage(request.toString())
            Log.d("Debug", "Annotation result: $annotation")

            // Extract the text from the annotation
            val fullTextAnnotation = annotation.asJsonArray[0]
                .asJsonObject["fullTextAnnotation"]
                ?.asJsonObject
                ?.get("text")
                ?.asString

            if (fullTextAnnotation.isNullOrEmpty()) {
                Log.e("Debug", "No text found in annotation")
                throw IllegalStateException("No text found in annotation")
            }

            Log.d("Debug", "Recognized text: $fullTextAnnotation")
            RequestState.Success(fullTextAnnotation)
        } catch (e: JsonParseException) {
            Log.e("Debug", "JSON Parsing error: ${e.message}")
            RequestState.Error("JSON Parsing error: ${e.message}")
        } catch (e: Exception) {
            Log.e("Debug", "Error during text recognition: ${e.message}")
            RequestState.Error(e.message.toString())
        }
    }

    private suspend fun annotateImage(requestJson: String): JsonElement {
        Log.d("Debug", "Sending request to Firebase Function")
        return suspendCancellableCoroutine { continuation ->
            functions.getHttpsCallable("annotateImage")
                .call(requestJson)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        try {
                            val result = task.result?.getData()
                            Log.d("Debug", "Raw Firebase response: $result")

                            if (result != null) {
                                val jsonElement = Gson().toJson(result).let { jsonString ->
                                    Log.d("Debug", "Parsed JSON string: $jsonString")
                                    JsonParser.parseString(jsonString)
                                }
                                continuation.resume(jsonElement) {}
                            } else {
                                throw IllegalStateException("Empty response from Firebase function")
                            }
                        } catch (e: Exception) {
                            Log.e("Debug", "Error parsing Firebase response: ${e.message}")
                            continuation.resumeWithException(e)
                        }
                    } else {
                        val exception = task.exception ?: Exception("Unknown error from Firebase")
                        Log.e("Debug", "Firebase function call failed: ${exception.message}")
                        continuation.resumeWithException(exception)
                    }
                }
        }
    }
}


