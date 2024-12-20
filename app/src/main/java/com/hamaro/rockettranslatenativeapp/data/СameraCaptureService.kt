package com.hamaro.rockettranslatenativeapp.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat

class CameraCaptureService {
    companion object {
        fun captureImage(
            imageCapture: ImageCapture,
            context: Context,
            onSuccess: (Uri) -> Unit,
            onError: (ImageCaptureException) -> Unit,
        ) {
            val name = "CameraxImage.jpeg"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
                }
            }
            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    context.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                .build()
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        outputFileResults.savedUri?.let {
                            println("Capture Success")
                            onSuccess(it)
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        println("Capture Failed $exception")
                        onError(exception)
                    }
                })
        }
    }
}

