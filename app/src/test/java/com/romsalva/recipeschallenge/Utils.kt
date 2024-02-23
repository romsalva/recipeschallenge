package com.romsalva.recipeschallenge

import com.romsalva.recipeschallenge.data.api.model.JsonRecipe
import com.romsalva.recipeschallenge.data.model.LatLng
import com.romsalva.recipeschallenge.data.model.Recipe
import com.romsalva.recipeschallenge.data.room.entity.RoomIngredient
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipe
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipeWithIngredients
import io.mockk.mockk

fun mockRecipe(id: Int = 1) = Recipe(
    id,
    "name",
    "description",
    listOf("ingredient1", "ingredient2"),
    "preparation",
    mockk(),
    LatLng(10.10, -10.10)
)

fun mockJsonRecipe(id: Int = 1) = JsonRecipe(
    id,
    "name",
    "description",
    listOf("ingredien"),
    "preparation",
    "http://placehold.co/600x400",
    10.10,
    -10.10
)

fun mockRoomRecipe(id: Int = 1) = RoomRecipeWithIngredients(
    RoomRecipe(
        id,
        "name",
        "description",
        "preparation",
        "http://placehold.co/600x400",
        10.10,
        -10.10
    ),
    listOf(
        RoomIngredient(
            0,
            id,
            "ingredient"
        )
    )
)
