package com.ichen.pocketbracket.timeline.components

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.ichen.pocketbracket.models.LocationRadius
import com.ichen.pocketbracket.utils.METERS_IN_MILE
import com.ichen.pocketbracket.utils.Z_INDEX_TOP
import com.ichen.pocketbracket.utils.getScaledRadius
import kotlin.math.roundToInt

const val RADIUS_MAX: Double = 1500.0
const val RADIUS_MIN: Double = 100.0
const val RADIUS_START: Double = RADIUS_MIN

@SuppressLint("PermissionLaunchedDuringComposition", "MissingPermission")
@ExperimentalPermissionsApi
@Composable
fun LocationPicker(
    coordinates: LatLng = LatLng(
        39.8283,
        -98.5795
    ),
    locationProvider: FusedLocationProviderClient,
    onNegativeButtonClick: () -> Unit,
    onPositiveButtonClick: (LocationRadius) -> Unit,
) {
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    val loadingLocation = remember { mutableStateOf(false) }
    val mapIsMoving = remember { mutableStateOf(false) }
    val locationRadius = remember {
        mutableStateOf(
            LocationRadius(
                coordinates, RADIUS_START
            )
        )
    }
    var sliderValue by remember { mutableStateOf(0f) }
    val map: MutableState<GoogleMap?> = remember { mutableStateOf(null) }
    val context = LocalContext.current
    var isFirstTime by remember { mutableStateOf(true) }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (isFirstTime) {
            /*

            this is kinda jank but
            we don't want to center on the user's location when they open this screen
            but we do want to center when the location permission is granted
            so this prevents the first time centering
            but lets us catch the permission being granted centering

             */
            isFirstTime = false
        } else if (locationPermissionState.status.isGranted) {
            loadingLocation.value = true
            locationProvider.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    locationRadius.value = LocationRadius(
                        LatLng(location.latitude, location.longitude),
                        locationRadius.value.radius
                    )
                    map.value?.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            locationRadius.value.center
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Error fetching location. Please make sure you have location permissions enabled.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                loadingLocation.value = false
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Error fetching location. Please make sure you have location permissions enabled.",
                    Toast.LENGTH_SHORT,
                ).show()
                loadingLocation.value = false
            }
        }
    }

    Box {
        MapView(map, mapIsMoving, locationRadius)
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
                Modifier.clickable {
                    onPositiveButtonClick(locationRadius.value)
                },
                color = MaterialTheme.colors.onPrimary
            )
        }
        LocationMarker(mapIsMoving.value)
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Box(
                Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.background)
                    .clickable {
                        if (!locationPermissionState.status.isGranted) { // TODO: rewrite location request
                            locationPermissionState.launchPermissionRequest()
                        } else {
                            loadingLocation.value = true
                            locationProvider.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    locationRadius.value = LocationRadius(
                                        LatLng(location.latitude, location.longitude),
                                        locationRadius.value.radius
                                    )
                                    map.value?.moveCamera(
                                        CameraUpdateFactory.newLatLng(
                                            locationRadius.value.center
                                        )
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error fetching location. Please make sure you have location permissions enabled.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                                loadingLocation.value = false
                            }.addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Error fetching location. Please make sure you have location permissions enabled.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                loadingLocation.value = false
                            }
                        }
                    }, contentAlignment = Alignment.Center
            ) {
                if(!loadingLocation.value) {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "get my location",
                        tint = MaterialTheme.colors.primary
                    )
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary, modifier = Modifier.fillMaxSize(.5f))
                }
            }
            Column(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = 48.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "${locationRadius.value.radius.roundToInt()} miles",
                    color = MaterialTheme.colors.onBackground
                )
                Slider(
                    value = sliderValue,
                    onValueChange = { newSliderValue ->
                        sliderValue = newSliderValue
                        locationRadius.value =
                            LocationRadius(
                                locationRadius.value.center,
                                getScaledRadius(newSliderValue)
                            )
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(thumbColor = MaterialTheme.colors.primary)
                )
            }
        }
    }
}

const val CIRCLE_SELECTED_TRANSPARENCY = .5f
const val CIRCLE_UNSELECTED_TRANSPARENCY = .2f

@Composable
fun MapView(
    map: MutableState<GoogleMap?>,
    mapIsMoving: MutableState<Boolean>,
    locationRadius: MutableState<LocationRadius>,
) {
    val circle: MutableState<Circle?> = remember { mutableStateOf(null) }
    val themeColors = MaterialTheme.colors

    circle.value?.radius = locationRadius.value.radius * METERS_IN_MILE

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            { context ->
                val mapView = MapView(context)
                mapView.getMapAsync { googleMap ->
                    map.value = googleMap
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            locationRadius.value.center, 6f
                        )
                    )
                    circle.value = googleMap.addCircle(
                        CircleOptions()
                            .center(locationRadius.value.center)
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
                            circle.value?.fillColor =
                                themeColors.primary.copy(alpha = CIRCLE_UNSELECTED_TRANSPARENCY)
                                    .toArgb()
                        }
                    }
                    googleMap.setOnCameraIdleListener {
                        if (mapIsMoving.value) {
                            mapIsMoving.value = false
                            if (map.value?.cameraPosition?.target != null) {
                                locationRadius.value = LocationRadius(
                                    map.value!!.cameraPosition.target,
                                    locationRadius.value.radius
                                )
                                circle.value?.center = map.value!!.cameraPosition.target
                            }
                            if (circle.value?.center != null) {
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
                    .alpha(.4f)
                    .background(Color.Black)
            )
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
        if (mapIsMoving) -10.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Box(
        Modifier
            .offset(y = offset + 3.dp)
            .zIndex(Z_INDEX_TOP), contentAlignment = Alignment.TopCenter
    ) {
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
            .size(5.dp)
            .clip(CircleShape)
            .background(Color.Black)
    )
}