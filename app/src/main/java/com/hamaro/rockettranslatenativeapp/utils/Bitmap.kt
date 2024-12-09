package com.roman_duda.rockettranslateapp.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class BitmapUtils {

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

    }
}