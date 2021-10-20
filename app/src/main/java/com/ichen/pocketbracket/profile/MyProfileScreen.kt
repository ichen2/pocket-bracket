package com.ichen.pocketbracket.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.apiKey
import com.ichen.pocketbracket.auth.AuthActivity
import com.ichen.pocketbracket.components.ShimmerAnimation
import com.ichen.pocketbracket.components.WebView
import com.ichen.pocketbracket.profile.components.UserSetting
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.ui.theme.medGrey
import com.ichen.pocketbracket.utils.API_KEY_STORAGE_KEY
import com.ichen.pocketbracket.utils.SHARED_PREFERENCES_KEY
import com.ichen.pocketbracket.utils.SetComposableFunction
import com.ichen.pocketbracket.utils.Status


@Composable
fun ColumnScope.MyProfileScreen(
    setDialogComposable: SetComposableFunction,
    viewModel: MyProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) =
    Column(
        Modifier
            .weight(1f)
            .background(MaterialTheme.colors.background)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = viewModel) {
            viewModel.getUserDetails(context)
        }
        if (viewModel.userDetails.value.status == Status.SUCCESS && viewModel.userDetails.value.data != null) {
            val userDetails = viewModel.userDetails.value.data
            Box(Modifier.verticalScroll(rememberScrollState())) {
                Image(
                    painter = if (userDetails?.imageUrls?.getOrNull(1) != null) rememberImagePainter(
                        data = userDetails.imageUrls[1],
                        builder = {
                            scale(Scale.FILL)
                            placeholder(R.drawable.image_unavailable)
                        }) else painterResource(id = R.drawable.image_unavailable),
                    contentDescription = "profile image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .background(medGrey),
                )
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(100.dp))
                    Row {
                        Image(
                            painter = if (userDetails?.imageUrls?.getOrNull(0) != null) rememberImagePainter(
                                data = userDetails.imageUrls[0],
                                builder = {
                                    scale(Scale.FILL)
                                    transformations(CircleCropTransformation())
                                    placeholder(R.drawable.image_unavailable)
                                }) else painterResource(id = R.drawable.image_unavailable),
                            contentDescription = "profile image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(
                                    CircleShape
                                )
                                .border(
                                    color = MaterialTheme.colors.background,
                                    width = 4.dp,
                                    shape = CircleShape
                                ),
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Spacer(Modifier.height(36.dp))
                            if (userDetails?.tag != null) {
                                Text(
                                    userDetails.tag,
                                    color = MaterialTheme.colors.onBackground,
                                    style = MaterialTheme.typography.h4
                                )
                            }
                            if (userDetails?.name != null) {
                                Text(userDetails.name, color = MaterialTheme.colors.onBackground)
                            }
                        }
                    }
                    if (userDetails?.location != null) {
                        Row(Modifier.padding(8.dp)) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = "user location",
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                userDetails.location,
                                color = MaterialTheme.colors.onBackground
                            )
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                    ) {
                        Text(
                            "User Settings",
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.h4
                        )
                        if (userDetails?.url != null) {
                            UserSetting(Icons.Filled.AccountCircle, "Edit Profile") {
                                setDialogComposable {
                                    WebView(userDetails.url) {
                                        setDialogComposable(null)
                                    }
                                }
                            }
                        }
                        UserSetting(Icons.Filled.Logout, "Log Out") {
                            apiKey = null
                            context.startActivity(Intent(context, AuthActivity::class.java))
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(16.dp)
                    ) {
                        Text(
                            "About",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.h4
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(text = buildAnnotatedString {
                            val text  = "Pocket Bracket is powered by the smash.gg API. For support, bug reports, or feature suggestions please contact "
                            val annotatedText = "pocketbracket@gmail.com"
                            append(text + annotatedText)
                            addStringAnnotation("URL", "mailto:$annotatedText", start = text.length, end = text.length + annotatedText.length)
                        }, color = MaterialTheme.colors.onSurface)
                    }
                }
            }
        } else if (viewModel.userDetails.value.status == Status.ERROR) {
            Text("Error loading user profile", color = MaterialTheme.colors.onBackground)
            Row(Modifier.clickable {
                apiKey = null
                context.startActivity(Intent(context, AuthActivity::class.java))
            }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "log out",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colors.onSurface
                )
                Spacer(Modifier.width(16.dp))
                Text("Log Out", color = MaterialTheme.colors.onSurface)
            }
        } else {
            MyProfileScreenLoading()
        }
    }

@Composable
fun MyProfileScreenLoading() = ShimmerAnimation { brush ->
    Box(
        Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.background)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(128.dp)
                .background(brush)
        )
        Column(Modifier.padding(16.dp)) {
            Spacer(Modifier.height(100.dp))
            Row {
                Box(
                    Modifier
                        .size(100.dp)
                        .background(brush, CircleShape)
                        .border(
                            color = MaterialTheme.colors.background,
                            width = 4.dp,
                            shape = CircleShape
                        )
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Spacer(Modifier.height(36.dp))
                    Box(
                        Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            .background(brush = brush)
                    )
                    Spacer(Modifier.height(16.dp))
                    Box(
                        Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            .background(brush = brush)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MyProfileScreenLoadingPreview() {
    PocketBracketTheme {
        MyProfileScreenLoading()
    }
}