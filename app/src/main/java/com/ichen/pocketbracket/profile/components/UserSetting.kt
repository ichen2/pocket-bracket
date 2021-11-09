package com.ichen.pocketbracket.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.components.WebView
import com.ichen.pocketbracket.utils.SetComposableFunction

@Composable
fun UserSetting(icon: ImageVector, name: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = name,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colors.primary
        )
        Spacer(Modifier.width(16.dp))
        Text(name, color = MaterialTheme.colors.onSurface)
    }
}