package com.ichen.pocketbracket.auth

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.ichen.pocketbracket.MainActivity
import com.ichen.pocketbracket.components.WebView
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.ui.theme.medGrey
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.Status

class AuthActivity : AppCompatActivity() {

    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketBracketTheme {
                val context = LocalContext.current
                var showWebView by remember { mutableStateOf(false) }
                if (!showWebView) {
                    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .padding(4.dp)
                            ) {
                                Text(text = "Open smash.gg")
                            }
                            Text(
                                text = "Enter your token below",
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.height(80.dp).navigationBarsWithImePadding()
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    value = viewModel.apiKeyText.value.data,
                                    onValueChange = { newApiKey ->
                                        viewModel.apiKeyText.value =
                                            Field(newApiKey, Status.NOT_STARTED)
                                    },
                                    singleLine = true,
                                    placeholder = { Text("Auth Token") },
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = MaterialTheme.colors.onBackground,
                                        placeholderColor = medGrey
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        viewModel.verifyApiKey(context)
                                    })
                                )
                                if (viewModel.apiKeyText.value.status == Status.ERROR) {
                                    Text(
                                        text = "Invalid auth token",
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Start,
                                        color = MaterialTheme.colors.error
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                            }
                            Button(modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .padding(4.dp), onClick = {
                                viewModel.verifyApiKey(context)
                            }) {
                                if (viewModel.apiKeyText.value.status == Status.LOADING) {
                                    CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
                                } else {
                                    Text("Submit auth token")
                                }
                            }
                        }
                    }
                } else {
                    WebView(SITE_ENDPOINT) {
                        showWebView = false
                    }
                }
            }
        }
    }
}