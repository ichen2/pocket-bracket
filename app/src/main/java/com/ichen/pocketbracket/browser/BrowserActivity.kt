package com.ichen.pocketbracket.browser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ichen.pocketbracket.components.WebView
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme

class BrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        setContent {
            PocketBracketTheme {
                WebView(url ?: "https://smash.gg/error") {
                    onBackPressed()
                }
            }
        }
    }
}