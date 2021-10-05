package com.ichen.pocketbracket.timeline.components

import android.os.Bundle
import android.view.DragEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.Circle
import com.ichen.pocketbracket.utils.LocationRadius
import com.ichen.pocketbracket.utils.METERS_IN_MILE

const val SLIDER_MAX = 1500f
const val SLIDER_MIN = 10f
const val SLIDER_START = 50f

@Composable
fun LocationPicker(onNegativeButtonClick: () -> Unit, onPositiveButtonClick: (LocationRadius) -> Unit) {
    val mapIsMoving = remember { mutableStateOf(false) }
    val locationRadius = remember {
        mutableStateOf(
            LocationRadius(
                LatLng(
                    39.8283,
                    -98.5795
                ), SLIDER_START
            )
        )
    }
    Box {
        MapView(mapIsMoving, locationRadius)
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Icon(
                Icons.Filled.Close,
                "close",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.clickable { onNegativeButtonClick() })
            Spacer(Modifier.weight(1f))
            Text(
                "Save",
                Modifier.clickable { onPositiveButtonClick(locationRadius.value) },
                color = MaterialTheme.colors.onPrimary
            )
        }
        LocationMarker(mapIsMoving.value)
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 48.dp, vertical = 16.dp)
        ) {
            Text(
                text = "${locationRadius.value.radius.roundToInt()} miles",
                color = MaterialTheme.colors.onBackground
            )
            Slider(
                value = locationRadius.value.radius,
                onValueChange = { newRadius -> locationRadius.value = LocationRadius(locationRadius.value.center, newRadius) },
                valueRange = SLIDER_MIN..SLIDER_MAX,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colors.primary)
            )
        }
    }
}


const val CIRCLE_SELECTED_TRANSPARENCY = .25f
const val CIRCLE_UNSELECTED_TRANSPARENCY = .1f

@Composable
fun MapView(mapIsMoving: MutableState<Boolean>, locationRadius: MutableState<LocationRadius>) {
    var map: GoogleMap? by remember { mutableStateOf(null) }
    val circle: MutableState<Circle?> = remember { mutableStateOf(null) }
    val themeColors = MaterialTheme.colors


    if (circle.value != null) circle.value!!.radius = locationRadius.value.radius * METERS_IN_MILE

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            { context ->
                val mapView = MapView(context)
                mapView.getMapAsync { googleMap ->
                    val center = LatLng(
                        39.8283,
                        -98.5795
                    )
                    map = googleMap
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            center, 6f
                        )
                    )
                    circle.value = googleMap.addCircle(
                        CircleOptions()
                            .center(center)
                            .radius(locationRadius.value.radius * METERS_IN_MILE)
                            .visible(true)
                            .fillColor(
                                themeColors.primary.copy(alpha = CIRCLE_SELECTED_TRANSPARENCY)
                                    .toArgb()
                            )
                            .strokeColor(Color.Transparent.toArgb())
                    )
                    googleMap.setOnCameraMoveStartedListener {
                        if (!mapIsMoving.value) {
                            mapIsMoving.value = true
                            circle.value!!.fillColor =
                                themeColors.primary.copy(alpha = CIRCLE_UNSELECTED_TRANSPARENCY)
                                    .toArgb()
                        }
                    }
                    googleMap.setOnCameraIdleListener {
                        if (mapIsMoving.value) {
                            locationRadius.value = LocationRadius(map!!.cameraPosition.target, locationRadius.value.radius)
                            mapIsMoving.value = false
                            circle.value!!.center = map?.cameraPosition?.target
                            if (circle.value!!.center != null) {
                                circle.value!!.fillColor =
                                    themeColors.primary.copy(alpha = CIRCLE_SELECTED_TRANSPARENCY)
                                        .toArgb()
                                circle.value!!.isVisible = true
                            }
                        }
                    }
                }
                mapView.onCreate(Bundle.EMPTY)
                mapView.onStart()
                mapView.onResume()
                mapView
            }, modifier = Modifier.fillMaxSize()
        )
        if (isSystemInDarkTheme()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .alpha(.5f)
                    .background(Color.Black)
            ) {

            }
        }
    }
}

@Composable
fun BoxScope.LocationMarker(mapIsMoving: Boolean) = Column(
    Modifier
        .align(Alignment.Center)
        .offset(y = -18.dp), horizontalAlignment = Alignment.CenterHorizontally
) {
    val offset by animateDpAsState(
        if (mapIsMoving) -8.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(Modifier.offset(y = offset), contentAlignment = Alignment.TopCenter) {
        Box(
            Modifier
                .width(2.dp)
                .height(32.dp)
                .background(MaterialTheme.colors.primary)
        )
        Box(contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
            )
            Box(
                Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onPrimary)
            )
        }
    }
    Box(
        Modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(Color.Black)
    )
}