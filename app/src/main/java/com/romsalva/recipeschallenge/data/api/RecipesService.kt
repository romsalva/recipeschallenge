package com.romsalva.recipeschallenge.data.api

import com.romsalva.recipeschallenge.data.api.model.JsonRecipe
import retrofit2.http.GET

interface RecipesService {
    @GET("recipes")
    suspend fun getRecipes(): List<JsonRecipe>
}