package com.romsalva.recipeschallenge.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RoomRecipeWithIngredients(
    @Embedded val recipe: RoomRecipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<RoomIngredient>
)
