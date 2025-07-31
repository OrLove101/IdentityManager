package com.OrLove.peoplemanager.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.OrLove.peoplemanager.R

@Composable
fun WarningDialog(
    onDismiss: () -> Unit,
    actionClick: () -> Unit,
    contentText: String
) {
    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = contentText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(height = 20.dp))
                Button(
                    onClick = actionClick
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        }
    )
}