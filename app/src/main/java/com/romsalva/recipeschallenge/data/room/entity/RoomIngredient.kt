package com.romsalva.recipeschallenge.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "ingredient",
    primaryKeys = [
        "order",
        "recipeId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = RoomRecipe::class,
            childColumns = ["recipeId"],
            parentColumns = ["id"]
        ),
    ]
)
data class RoomIngredient(
    val order: Int,
    val recipeId: Int,
    val ingredient: String
)
