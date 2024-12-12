package com.roman_duda.rockettranslateapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import android.net.Uri as Uri1

class ImageUtils {

    companion object {

        fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
            val originalWidth = bitmap.width
            val originalHeight = bitmap.height
            var resizedWidth = maxDimension
            var resizedHeight = maxDimension
            if (originalHeight > originalWidth) {
                resizedHeight = maxDimension
                resizedWidth =
                    (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
            } else if (originalWidth > originalHeight) {
                resizedWidth = maxDimension
                resizedHeight =
                    (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
            } else if (originalHeight == originalWidth) {
                resizedHeight = maxDimension
                resizedWidth = maxDimension
            }
            return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
        }

        fun convertToBase64(bitmap : Bitmap) : String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
            return  Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        }

        fun uriToBase64(context: Context, uri: Uri1): String? {
            return try {
                // Open an InputStream from the URI
                val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null

                // Convert the InputStream to a byte array
                val byteArray = inputStream.readBytes()

                // Encode the byte array to a Base64 string
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

    }
}

fun String.decodeBase64ToByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT) // Decode base64 string to byte array
}

fun ByteArray.toImageBitmap() = BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
