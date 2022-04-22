package com.ichen.pocketbracket.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ichen.pocketbracket.R

@Composable
fun ErrorSplash(message: String, isCritical: Boolean = true) = Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top){
    Icon(
        Icons.Filled.Error,
        contentDescription = "error",
        modifier = Modifier
            .size(48.dp),
        tint = if(isCritical) MaterialTheme.colors.error else MaterialTheme.colors.secondary
    )
    Spacer(Modifier.height(8.dp))
    Text(message, textAlign = TextAlign.Center)
    //Image(painter = rememberImagePainter(data = R.drawable.gorf), "gorf", modifier = Modifier.size(200.dp))
    //Text("Sorry about that - please enjoy this picture of Gorf", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center)
}

@Preview
@Composable
fun ErrorSplashPreview() = Column(Modifier.fillMaxSize()) {
    ErrorSplash(message = "Error - Could not fetch tournament results")
}