package com.ichen.pocketbracket.components

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebViewClient




@Composable
fun WebView(url: String, close: () -> Unit) = Column(Modifier.fillMaxSize()) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colors.primary)
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back button", tint = MaterialTheme.colors.onPrimary, modifier = Modifier
            .size(48.dp)
            .clickable { close() })
    }
    AndroidView(modifier = Modifier
        .fillMaxWidth()
        .weight(1f), factory = { context ->
        android.webkit.WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
    })
}

@Composable
@Preview
fun WebViewPreview() = Column(
    Modifier
        .fillMaxSize()
        .background(Color.Red)) {
    var showWebView by remember { mutableStateOf(true) }
    if(showWebView) {
        WebView("https://smash.gg/tournament/tired-of-0-2-3") { showWebView = false }
    } else {
        Text("WebView closed")
    }
}