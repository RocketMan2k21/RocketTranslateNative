package com.hamaro.rockettranslatenativeapp.ui.common

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hamaro.rockettranslatenativeapp.ui.theme.cameraPreviewIconColor

@Composable
fun FeedbackIconButton(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val emailAddress = "support@yourapp.com"
    val emailSubject = "Feedback"

    Box(
        modifier = modifier
    ) {
        // Icon Button
        IconButton(onClick = { expanded = true }) {
            Icon(
                modifier = Modifier
                    .size(30.dp),
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Menu",
                tint = cameraPreviewIconColor
            )
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                onClick = {
                    expanded = false // Close menu
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    }
                    if (emailIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(emailIntent)
                    } else {
                        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}