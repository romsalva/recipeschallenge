package com.romsalva.recipeschallenge.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RoomRecipe(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val preparation: String,
    val imageUri: String,
    val lat: Double,
    val lng: Double
)
