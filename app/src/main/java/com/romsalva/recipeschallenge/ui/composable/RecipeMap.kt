package com.romsalva.recipeschallenge.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.romsalva.recipeschallenge.ui.stateholder.UiState

@Composable
fun RecipeMap(
    recipeLocation: UiState.RecipeLocation,
    modifier: Modifier = Modifier,
    zoom: Float = 10.0f
) {
    val latLng = LatLng(recipeLocation.lat, recipeLocation.lng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, zoom)
    }
    Box {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(latLng),
                title = "Origin"
            )
        }
        Surface(
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            cameraPositionState.position.target.let {
                Text(
                    text = "%.4f,%.4f".format(it.latitude, it.longitude),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRecipeMap() {
    RecipeMap(UiState.RecipeLocation(0, -34.6072598, -58.4515826))
}