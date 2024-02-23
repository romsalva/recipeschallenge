package com.romsalva.recipeschallenge.data.api.model

data class JsonRecipe(
    val id: Int,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val preparation: String,
    val imageUrl: String,
    val lat: Double,
    val lng: Double
)
