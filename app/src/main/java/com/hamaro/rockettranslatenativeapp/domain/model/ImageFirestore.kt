package com.hamaro.rockettranslatenativeapp.domain.model

data class ImageHistory(
    val images : List<ImageFirestore>
)

data class ImageFirestore (
    val imageBaseEncoded : String,
    val createdAt : String
)