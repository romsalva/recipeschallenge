package com.romsalva.recipeschallenge.data.datasource.local

import com.romsalva.recipeschallenge.data.model.Recipe
import com.romsalva.recipeschallenge.data.room.dao.RecipesDao
import com.romsalva.recipeschallenge.data.toRecipe
import com.romsalva.recipeschallenge.data.toRecipeList
import com.romsalva.recipeschallenge.data.toRoomIngredients
import com.romsalva.recipeschallenge.data.toRoomRecipe
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipesLocalDataSource @Inject constructor(
    private val dao: RecipesDao
) {
    fun getAllRecipes(): Flow<List<Recipe>> =
        dao.getAll().map { it.toRecipeList() }

    fun searchRecipesByNameOrIngredients(name: String): Flow<List<Recipe>> =
        dao.findByNameOrIngredients(name).map { it.toRecipeList() }

    fun getRecipe(id: Int): Flow<Recipe> =
        dao.findById(id).map { it.toRecipe() }

    suspend fun saveAll(recipes: List<Recipe>) {
        recipes.forEach {
            dao.insertRecipe(it.toRoomRecipe())
            dao.insertIngredients(it.toRoomIngredients())
        }
    }

}