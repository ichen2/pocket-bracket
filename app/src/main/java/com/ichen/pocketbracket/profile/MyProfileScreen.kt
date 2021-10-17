package com.ichen.pocketbracket.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.auth.AuthActivity
import com.ichen.pocketbracket.timeline.TournamentsTimelineViewModel
import com.ichen.pocketbracket.utils.Status


@Composable
fun ColumnScope.MyProfileScreen(viewModel: MyProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) =
    Column(
        Modifier
            .weight(1f)
            .background(MaterialTheme.colors.background)
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = viewModel) {
            viewModel.getUserDetails(context)
        }
        if (viewModel.userDetails.value.status == Status.SUCCESS && viewModel.userDetails.value.data != null) {
            val userDetails = viewModel.userDetails.value.data
            Box(Modifier.height(225.dp)) {
                Image(
                    painter = rememberImagePainter(
                        data = userDetails?.imageUrls?.getOrNull(1),
                        builder = {
                            scale(Scale.FILL)
                            placeholder(R.drawable.image_unavailable)
                        }),
                    contentDescription = "profile image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                )
                if (userDetails?.imageUrls?.getOrNull(0) != null) Image(
                    painter = rememberImagePainter(
                        data = userDetails.imageUrls[0],
                        builder = {
                            scale(Scale.FIT)
                            placeholder(R.drawable.image_unavailable)
                        }),
                    contentDescription = "profile image",
                    modifier = Modifier
                        .offset(x = 10.dp, y = 75.dp)
                        .size(150.dp)
                        .clip(
                            CircleShape
                        )
                        .border(
                            color = MaterialTheme.colors.background,
                            width = 2.dp,
                            shape = CircleShape
                        ),
                )
            }
            Column {
                if (userDetails?.name != null) {
                    Text(userDetails.name, color = MaterialTheme.colors.onBackground)
                }
                if (userDetails?.location != null) {
                    Row {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = "user location",
                            tint = MaterialTheme.colors.primary
                        )
                        Text(userDetails.location, color = MaterialTheme.colors.onBackground)
                    }
                }
                Button(onClick = {
                    if(context is Activity) {
                        context.getPreferences(Context.MODE_PRIVATE).edit()
                            .remove("API_KEY").apply()
                    }
                    context.startActivity(Intent(context, AuthActivity::class.java))
                }) {
                    Text("Log Out")
                }
            }
        } else if (viewModel.userDetails.value.status == Status.ERROR) {
            Text("Error", color = MaterialTheme.colors.onBackground)
        } else {
            Text("Loading", color = MaterialTheme.colors.onBackground)
        }
    }