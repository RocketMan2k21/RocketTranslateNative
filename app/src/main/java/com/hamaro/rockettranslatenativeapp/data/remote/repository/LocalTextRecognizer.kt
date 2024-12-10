package com.hamaro.rockettranslatenativeapp.data.remote.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hamaro.rockettranslatenativeapp.domain.TextRecognizer
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class LocalTextRecognizer : TextRecognizer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    /**
     * Recognize text from a Bitmap image.
     * @param bitmap The input Bitmap image.
     * @return RequestState containing recognized text or an error message.
     */
    override suspend fun recognizeText(bitmap: Bitmap): RequestState<String> {
        return try {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val result = annotateImage(inputImage) // Suspend function for text recognition
            RequestState.Success(result.text)
        } catch (e: Exception) {
            Log.e("LocalTextRecognizer", "Error in recognizeText: ${e.message}", e)
            RequestState.Error(e.message ?: "Unknown error")
        }
    }

    /**
     * Annotate an InputImage to extract text using ML Kit.
     * @param inputImage The InputImage to process.
     * @return The recognized text result.
     */
    private suspend fun annotateImage(inputImage: InputImage): Text {
        return suspendCancellableCoroutine { continuation ->
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    if (!continuation.isCancelled) {
                        continuation.resume(visionText) {}
                    }
                }
                .addOnFailureListener { exception ->
                    if (!continuation.isCancelled) {
                        continuation.resumeWithException(exception)
                    }
                }
        }
    }
}
