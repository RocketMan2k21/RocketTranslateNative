package com.hamaro.rockettranslatenativeapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hamaro.rockettranslatenativeapp.ui.theme.Typography

@Composable
fun CustomTopBarWithReturn(
    title : String,
    onClickBack : () -> Unit,
) {

    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            text = title,
            style = Typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }

}

@Preview
@Composable
fun PreviewCustomTopBarWithReturn() {
    CustomTopBarWithReturn(
        title = "Image History"
    ) { }
}