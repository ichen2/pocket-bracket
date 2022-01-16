package com.ichen.pocketbracket.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.models.DateRange
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.utils.mergeAddress

@Composable
fun Banner(tournament: Tournament) {
    if(tournament.secondaryImageUrl != null) {
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
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if(tournament.primaryImageUrl != null) {
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
            Spacer(Modifier.width(16.dp))
        }
        Column {
            Text(text = tournament.name, style = MaterialTheme.typography.h4, color = MaterialTheme.colors.onSurface)
            if (tournament.startAt != null && tournament.endAt != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Event,
                        contentDescription = "date icon",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = DateRange(tournament.startAt, tournament.endAt).toString(),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                    )
                }
            }
            val mergedAddress =
                mergeAddress(tournament.city, tournament.addrState, tournament.countryCode)
            if (mergedAddress != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "location icon",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = mergedAddress,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                    )
                }
            }
            // TODO: this actually isn't always an email, so I'll have to detect the type and change the link/icon based on that
            if (tournament.primaryContact != null) {
                val annotatedString = buildAnnotatedString {
                    val annotatedText = tournament.primaryContact
                    append(annotatedText)
                    addStringAnnotation(
                        "URL",
                        if(annotatedText.contains("@")) "mailto:$annotatedText" else annotatedText,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if(tournament.primaryContact.contains("@")) Icons.Filled.Email else Icons.Filled.ContactSupport,
                        contentDescription = "email icon",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    ClickableText(text = annotatedString, onClick = {
                        annotatedString
                            .getStringAnnotations("URL", it, it)
                            .firstOrNull()?.let { stringAnnotation ->
                                uriHandler.openUri(stringAnnotation.item)
                            }
                    }, maxLines = 1, overflow = TextOverflow.Clip)
                }
            }
        }
    }
}