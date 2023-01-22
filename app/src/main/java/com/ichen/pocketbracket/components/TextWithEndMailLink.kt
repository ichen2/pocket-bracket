package com.ichen.pocketbracket.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun TextWithEndMailLink(
    body: String,
    mailLink: String,
    context: Context,
) {
    val annotatedText = buildAnnotatedString {
        append(body + mailLink)
        addStringAnnotation(
            tag = "URL",
            annotation = "mailto:$mailLink",
            start = body.length,
            end = body.length + mailLink.length,
        )
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = MaterialTheme.typography.body1.fontSize,
                textDecoration = TextDecoration.None
            ), start = 0, end = body.length
        )
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary,
                fontSize = MaterialTheme.typography.body1.fontSize,
                textDecoration = TextDecoration.Underline
            ), start = body.length, end = body.length + mailLink.length
        )
    }
    ClickableText(
        text = annotatedText,
        onClick = {
              context.startActivity(
                  Intent(Intent.ACTION_SEND).apply {
                      type = "plain/text"
                      putExtra(Intent.EXTRA_EMAIL, arrayOf(mailLink))
                      putExtra(Intent.EXTRA_SUBJECT, "App feedback")
                  }
              )
        },
    )
}
