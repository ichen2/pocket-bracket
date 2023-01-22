package com.ichen.pocketbracket.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.auth.AuthActivity
import com.ichen.pocketbracket.home.apiKey
import com.ichen.pocketbracket.utils.CONTACT_EMAIL

@Composable
fun ErrorSplash(message: String, isCritical: Boolean = false) = Column(
    modifier = Modifier.fillMaxSize().padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
){
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface)
        .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = "error",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colors.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5,
        )
        if (isCritical) {
            Spacer(Modifier.height(16.dp))
            TextWithEndMailLink(
                body = "Your auth token may be invalid. Please try logging out, and logging back in with a new token. If the issue persists please contact ",
                mailLink = CONTACT_EMAIL,
                context = context,
            )
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .clickable {
                        apiKey = null
                        context.startActivity(
                            Intent(context, AuthActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        )
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colors.onPrimary,
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Log Out",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                )
            }
        }
    }
}

@Preview
@Composable
fun ErrorSplashPreview() = Column(Modifier.fillMaxSize()) {
    ErrorSplash(message = "Error - Could not fetch tournament results")
}

@Preview
@Composable
fun CriticalErrorSplashPreview() = Column(Modifier.fillMaxSize()) {
    ErrorSplash(message = "Critical Error - Could not fetch tournament results", isCritical = true,)
}