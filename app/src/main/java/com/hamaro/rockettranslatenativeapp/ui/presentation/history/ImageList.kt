package com.hamaro.rockettranslatenativeapp.ui.presentation.history

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hamaro.rockettranslatenativeapp.domain.model.ImageFirestore
import com.hamaro.rockettranslatenativeapp.ui.theme.onPrimaryTextColor
import com.roman_duda.rockettranslateapp.utils.decodeBase64ToByteArray
import com.roman_duda.rockettranslateapp.utils.toImageBitmap

@Composable
fun GridContent(
    modifier: Modifier = Modifier,
    images : List<ImageFirestore>,
    onImageClick : (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = images.count()
        ) { index ->
            val image = images[index]
            ImageItem(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onImageClick(
                            image.imageBaseEncoded
                        )
                    },
                image = image,
            )
        }
    }
}

@Composable
fun ImageItem(modifier: Modifier = Modifier, image: ImageFirestore) {
    Column (
        modifier = modifier
    ) {

        Log.d("Debug", "does image has image: ${image.imageBaseEncoded.isNotBlank()}")
        Log.d("Debug", image.createdAt)

        Image(
            bitmap = image.imageBaseEncoded.decodeBase64ToByteArray().toImageBitmap(),
            contentDescription = "My image",
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = image.createdAt,
            color = onPrimaryTextColor
        )
    }
}

@Preview
@Composable
fun PreviewImageItem() {
    ImageItem(
        image = ImageFirestore(
            imageBaseEncoded = "",
            createdAt = "24/12/2024"
        )
    )
}

