package com.ichen.pocketbracket.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ichen.pocketbracket.R

@Composable
fun ErrorSplash(message: String) = Column(Modifier.fillMaxSize().background(MaterialTheme.colors.background).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
    Text(message, color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center)
    //Image(painter = rememberImagePainter(data = R.drawable.gorf), "gorf", modifier = Modifier.size(200.dp))
    //Text("Sorry about that - please enjoy this picture of Gorf", color = MaterialTheme.colors.onBackground, textAlign = TextAlign.Center)
}

@Preview
@Composable
fun ErrorSplashPreview() = Column(Modifier.fillMaxSize()) {
    ErrorSplash(message = "Error - Could not fetch tournament results")
}