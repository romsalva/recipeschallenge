package com.romsalva.recipeschallenge.data.datasource.remote

import com.romsalva.recipeschallenge.data.api.RecipesService
import com.romsalva.recipeschallenge.data.model.Recipe
import com.romsalva.recipeschallenge.data.toRecipe
import javax.inject.Inject

class RecipesRemoteDataSource @Inject constructor(
    private val service: RecipesService
) {
    suspend fun getAllRecipes(): List<Recipe> =
        service.getRecipes().map { it.toRecipe() }
}