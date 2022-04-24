package com.ichen.pocketbracket.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.models.Attendee
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.openBrowser

@Composable
fun AttendeeProfile(attendee: Attendee, tournamentSlug: String) {
    val context = LocalContext.current
    Row(
        Modifier.fillMaxWidth().clickable { openBrowser(context, "${SITE_ENDPOINT}/${tournamentSlug}/attendee/${attendee.id}") }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (attendee.imageUrl != null) Image(
            painter = rememberImagePainter(data = attendee.imageUrl, builder = {
                scale(Scale.FILL)
                placeholder(R.drawable.image_unavailable)
                crossfade(true)
            }),
            contentDescription = "attendee profile image",
            modifier = Modifier.size(32.dp).clip(CircleShape),
            contentScale = ContentScale.Crop,
        ) else {
            Box(
                Modifier.size(32.dp).clip(CircleShape).background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${attendee.tag.getOrNull(0)?.toUpperCase() ?: "?"}",
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Text(
            attendee.tag,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun AttendeeProfileLoading(brush: Brush) = Row(modifier = Modifier
    .fillMaxWidth()
    .background(brush = brush).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
    Box(Modifier.size(32.dp).clip(CircleShape).background(MaterialTheme.colors.background))
    Spacer(Modifier.width(8.dp))
    Box(Modifier.width(200.dp).height(16.dp).background(MaterialTheme.colors.background))
}