package com.ichen.pocketbracket.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.utils.SITE_ENDPOINT


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketBracketTheme {
                AuthScreen(showWebView = {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(SITE_ENDPOINT))
                        startActivity(intent)
                })
            }
        }
    }
}