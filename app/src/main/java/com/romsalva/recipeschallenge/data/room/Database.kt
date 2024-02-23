package com.romsalva.recipeschallenge.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romsalva.recipeschallenge.data.room.dao.RecipesDao
import com.romsalva.recipeschallenge.data.room.entity.RoomIngredient
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipe

@Database(
    version = 1,
    entities = [
        RoomRecipe::class,
        RoomIngredient::class
    ]
)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}