package com.romsalva.recipeschallenge.data

import androidx.core.net.toUri
import com.romsalva.recipeschallenge.data.api.model.JsonRecipe
import com.romsalva.recipeschallenge.data.model.LatLng
import com.romsalva.recipeschallenge.data.model.Recipe
import com.romsalva.recipeschallenge.data.room.entity.RoomIngredient
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipe
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipeWithIngredients

fun JsonRecipe.toRecipe() = Recipe(
    id,
    name,
    description,
    ingredients.toList(),
    preparation,
    imageUrl.toUri(),
    LatLng(lat, lng)
)

fun Recipe.toRoomRecipe() = RoomRecipe(
    id,
    name,
    description,
    preparation,
    imageUri.toString(),
    origin.lat,
    origin.lng
)

fun Recipe.toRoomIngredients() = ingredients.mapIndexed { index, ingredient ->
    RoomIngredient(
        order = index,
        recipeId = id,
        ingredient = ingredient
    )
}

fun RoomRecipeWithIngredients.toRecipe() = Recipe(
    recipe.id,
    recipe.name,
    recipe.description,
    ingredients.sortedBy { it.order }.map { it.ingredient },
    recipe.preparation,
    recipe.imageUri.toUri(),
    LatLng(recipe.lat, recipe.lng)
)

fun Iterable<RoomRecipeWithIngredients>.toRecipeList() = map { it.toRecipe() }

