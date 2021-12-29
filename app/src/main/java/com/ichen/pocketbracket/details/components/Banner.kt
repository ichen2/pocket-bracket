package com.ichen.pocketbracket.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.models.DateRange
import com.ichen.pocketbracket.models.Tournament

@Composable
fun Banner(tournament: Tournament) {
    Image(
        painter = rememberImagePainter(data = tournament.secondaryImageUrl, builder = {
            size(OriginalSize)
            scale(Scale.FILL)
            placeholder(R.drawable.image_unavailable)
            crossfade(true)
        }),
        contentDescription = "banner image",
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Image(
            painter = rememberImagePainter(data = tournament.primaryImageUrl, builder = {
                size(OriginalSize)
                scale(Scale.FIT)
                placeholder(R.drawable.image_unavailable)
                crossfade(true)
            }),
            contentDescription = "tournament image",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(text = tournament.name, style = MaterialTheme.typography.h4)
            if(tournament.startAt != null && tournament.endAt != null) Text(text = DateRange(tournament.startAt, tournament.endAt).toString(), style = MaterialTheme.typography.body1)
            if(tournament.venueAddress != null) Text(text = tournament.venueAddress, style = MaterialTheme.typography.body1)
            if(tournament.primaryContact != null) {
                val annotatedString = buildAnnotatedString {
                    val annotatedText = tournament.primaryContact
                    append(annotatedText)
                    addStringAnnotation(
                        "URL",
                        "mailto:$annotatedText",
                        start = 0,
                        end = annotatedText.length
                    )
                    addStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            textDecoration = TextDecoration.Underline
                        ), start = 0, end = annotatedText.length
                    )
                }
                val uriHandler = LocalUriHandler.current
                ClickableText(text = annotatedString, onClick = {
                    annotatedString
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                })
            }
        }
    }
}