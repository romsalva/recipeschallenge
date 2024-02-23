package com.romsalva.recipeschallenge.data.model

import android.net.Uri

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val preparation: String,
    val imageUri: Uri,
    val origin: LatLng
)
