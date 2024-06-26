package com.ichen.pocketbracket.profile

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.auth.AuthActivity
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.components.ShimmerAnimation
import com.ichen.pocketbracket.components.TextWithEndMailLink
import com.ichen.pocketbracket.home.apiKey
import com.ichen.pocketbracket.profile.components.UserSetting
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.ui.theme.medGrey
import com.ichen.pocketbracket.utils.CONTACT_EMAIL
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.openBrowser


@Composable
fun ColumnScope.MyProfileScreen(
    viewModel: MyProfileViewModel = viewModel()
) =
    Column(
        modifier = Modifier
            .weight(1f)
            .background(MaterialTheme.colors.background)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = viewModel) {
            viewModel.getUserDetails(context)
        }
        if (viewModel.userDetails.value.status == Status.SUCCESS && viewModel.userDetails.value.data != null) {
            val userDetails = viewModel.userDetails.value.data
            Box(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter,
            ) {
                Image(
                    contentScale = ContentScale.FillWidth,
                    painter = if (userDetails?.bannerImageUrl != null) rememberImagePainter(
                        data = userDetails.bannerImageUrl,
                        builder = {
                            size(OriginalSize)
                            placeholder(R.drawable.image_unavailable)
                        }) else ColorPainter(MaterialTheme.colors.primary),
                    contentDescription = "banner image",
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
                        if(userDetails?.profileImageUrl != null) {
                            Image(
                                painter = rememberImagePainter(
                                    data = userDetails.profileImageUrl,
                                    builder = {
                                        scale(Scale.FILL)
                                        transformations(CircleCropTransformation())
                                        placeholder(R.drawable.image_unavailable)
                                    }),
                                contentDescription = "profile image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(
                                        color = MaterialTheme.colors.background,
                                        width = 4.dp,
                                        shape = CircleShape
                                    ),
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${userDetails?.tag?.getOrNull(0)?.toUpperCase() ?: "?"}",
                                    fontSize = 48.sp,
                                    lineHeight = 20.sp,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Spacer(Modifier.height(36.dp))
                            if (userDetails?.tag != null) {
                                Text(
                                    text = userDetails.tag,
                                    color = MaterialTheme.colors.onBackground,
                                    style = MaterialTheme.typography.h4
                                )
                            }
                            if (userDetails?.name != null) {
                                Text(
                                    text  =userDetails.name,
                                    color = MaterialTheme.colors.onBackground,
                                )
                            }
                        }
                    }
                    if (userDetails?.location != null) {
                        Row(Modifier.padding(8.dp)) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "user location",
                                tint = MaterialTheme.colors.primary,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = userDetails.location,
                                color = MaterialTheme.colors.onBackground,
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
                            text = "User Settings",
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.h4,
                        )
                        if (userDetails?.url != null) {
                            UserSetting(Icons.Filled.AccountCircle, "Edit Profile") {
                                openBrowser(context, userDetails.url)
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
                            text = "About",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.h4
                        )
                        Spacer(Modifier.height(16.dp))
                        TextWithEndMailLink(
                            body = "Pocket Bracket is powered by the start.gg API. For support, bug reports, or feature suggestions please contact ",
                            mailLink = CONTACT_EMAIL,
                            context = context,
                        )
                    }
                }
            }
        } else if (viewModel.userDetails.value.status == Status.ERROR) {
            ErrorSplash(
                message = "Could not fetch profile from start.gg",
                isCritical = true,
            )
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