package com.ichen.pocketbracket.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ichen.pocketbracket.MainActivity
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PocketBracketTheme {
                val apiKeyText = remember { mutableStateOf("") }
                val context = LocalContext.current
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = apiKeyText.value,
                        onValueChange = { newApiKey ->
                            apiKeyText.value = newApiKey
                        },
                        colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colors.onPrimary)
                    )
                    Button(onClick = {
                        startActivity(Intent(context, MainActivity::class.java).apply {
                            putExtra("API_KEY", apiKeyText.value)
                        })
                    }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}