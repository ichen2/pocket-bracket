package com.ichen.pocketbracket.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ichen.pocketbracket.components.WebView
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.utils.SITE_ENDPOINT

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketBracketTheme {
                var showWebView by remember { mutableStateOf(false) }
                if (!showWebView) {
                    AuthScreen(showWebView = {
                        showWebView = true
                    })
                } else {
                    WebView(SITE_ENDPOINT) {
                        showWebView = false
                    }
                }
            }
        }
    }
}