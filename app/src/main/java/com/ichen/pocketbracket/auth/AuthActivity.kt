package com.ichen.pocketbracket.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.MainActivity
import com.ichen.pocketbracket.components.WebView
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.ui.theme.medGrey

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PocketBracketTheme {
                val apiKeyText = remember { mutableStateOf("") }
                val context = LocalContext.current
                var showWebView by remember { mutableStateOf(false) }
                if (!showWebView) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                            .padding(64.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "In order to connect your account with smash.gg, PocketBracket needs an authentication token. To generate this token:",
                            color = MaterialTheme.colors.onBackground
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = buildAnnotatedString {
                                append("1. Sign in to ")
                                withStyle(
                                    style =
                                    SpanStyle(color = MaterialTheme.colors.primary)
                                ) {
                                    append("smash.gg")
                                }
                            },
                            color = MaterialTheme.colors.onBackground
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = buildAnnotatedString {
                                append("2. Open ")
                                withStyle(
                                    style =
                                    SpanStyle(color = MaterialTheme.colors.primary)
                                ) {
                                    append("Developer Settings")
                                }
                            },
                            color = MaterialTheme.colors.onBackground
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = buildAnnotatedString {
                                append("3. Click ")
                                withStyle(
                                    style =
                                    SpanStyle(color = MaterialTheme.colors.primary)
                                ) {
                                    append("Generate new token")
                                }
                            },
                            color = MaterialTheme.colors.onBackground
                        )
                        Button(
                            onClick = { showWebView = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Open smash.gg")
                        }
                        Text(
                            text = "Enter your token below",
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = apiKeyText.value,
                            onValueChange = { newApiKey ->
                                apiKeyText.value = newApiKey
                            },
                            placeholder = { Text("Auth Token") },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.onBackground,
                                placeholderColor = medGrey
                            )
                        )
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            startActivity(Intent(context, MainActivity::class.java).apply {
                                putExtra("API_KEY", apiKeyText.value)
                            })
                        }) {
                            Text("Submit Auth Token")
                        }
                    }
                } else {
                    WebView("") {
                        showWebView = false
                    }
                }
            }
        }
    }
}