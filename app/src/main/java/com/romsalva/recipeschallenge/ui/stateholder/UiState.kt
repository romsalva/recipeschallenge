package com.romsalva.recipeschallenge.ui.stateholder

import android.net.Uri
import com.romsalva.recipeschallenge.data.model.Recipe
import kotlin.Double
import kotlin.Int
import kotlin.String

object UiState {

    data class RecipeListItem(
        val id: Int,
        val name: String,
        val imageUri: Uri
    )

    data class RecipeDetail(
        val id: Int,
        val name: String,
        val imageUri: Uri,
        val description: String,
        val ingredients: List<String>,
        val preparation: String
    )

    data class RecipeLocation(
        val id: Int,
        val lat: Double,
        val lng: Double
    )

    sealed class Error(val message: String) {
        data object None : Error("")
        class Network(message: String) : Error(message)
        class Database(message: String) : Error(message)
        class Other(message: String) : Error(message)
    }

}

fun Recipe.asUiStateRecipeListItem() = UiState.RecipeListItem(
    id,
    name,
    imageUri
)

fun Recipe.asUiStateRecipeDetail() = UiState.RecipeDetail(
    id,
    name,
    imageUri,
    description,
    ingredients,
    preparation
)

fun Recipe.asUiStateRecipeLocation() = UiState.RecipeLocation(
    id,
    origin.lat,
    origin.lng
)