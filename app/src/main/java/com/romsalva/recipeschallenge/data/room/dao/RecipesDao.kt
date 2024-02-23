package com.romsalva.recipeschallenge.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.romsalva.recipeschallenge.data.room.entity.RoomIngredient
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipe
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipeWithIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Transaction
    @Query("SELECT * FROM recipe")
    fun getAll(): Flow<List<RoomRecipeWithIngredients>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM recipe
        JOIN ingredient ON recipe.id=ingredient.recipeId
        WHERE recipe.name LIKE '%' || :text || '%' OR ingredient.ingredient LIKE '%' || :text || '%'
        GROUP BY id
   """
    )
    fun findByNameOrIngredients(text: String): Flow<List<RoomRecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM recipe WHERE id=:id LIMIT 1")
    fun findById(id: Int): Flow<RoomRecipeWithIngredients>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipes: RoomRecipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(recipes: List<RoomIngredient>)
}