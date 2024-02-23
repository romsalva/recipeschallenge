package com.romsalva.recipeschallenge.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.romsalva.recipeschallenge.ui.composable.screen.RecipeMapScreen

private const val recipeMapBaseRoute = "recipeMap"
private const val recipeMapRecipeId = "recipeId"

fun NavGraphBuilder.recipeMapScreen(
    onNavigateUp: () -> Unit
) {
    composable(
        route = "$recipeMapBaseRoute/{${recipeMapRecipeId}}",
        arguments = listOf(navArgument(recipeMapRecipeId) { type = NavType.IntType })
    ) { navBackStackEntry ->
        RecipeMapScreen(
            viewModel = hiltViewModel(navBackStackEntry),
            onNavigateUp = onNavigateUp
        )
    }
}

fun NavController.navigateToRecipeMap(recipeId: Int) {
    navigate("$recipeMapBaseRoute/$recipeId")
}

class RecipeMapArgs(val recipeId: Int) {
    constructor(savedStateHandle: SavedStateHandle) : this(checkNotNull(savedStateHandle.get<Int>(recipeMapRecipeId)))
}