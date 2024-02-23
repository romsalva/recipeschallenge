package com.romsalva.recipeschallenge.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.romsalva.recipeschallenge.ui.composable.screen.RecipeDetailScreen
import com.romsalva.recipeschallenge.ui.stateholder.UiState

private const val recipeDetailBaseRoute = "recipeDetail"
private const val recipeDetailRecipeId = "recipeId"

fun NavGraphBuilder.recipeDetailScreen(
    onNavigateUp: () -> Unit,
    onNavigateToMap: (UiState.RecipeDetail) -> Unit
) {
    composable(
        route = "$recipeDetailBaseRoute/{${recipeDetailRecipeId}}",
        arguments = listOf(navArgument(recipeDetailRecipeId) { type = NavType.IntType })
    ) { navBackStackEntry ->
        RecipeDetailScreen(
            viewModel = hiltViewModel(navBackStackEntry),
            onNavigateUp = onNavigateUp,
            onNavigateToMap = onNavigateToMap
        )
    }
}

fun NavController.navigateToRecipeDetail(recipeId: Int) {
    navigate("$recipeDetailBaseRoute/$recipeId")
}

class RecipeDetailArgs(val recipeId: Int) {
    constructor(savedStateHandle: SavedStateHandle) : this(checkNotNull(savedStateHandle.get<Int>(recipeDetailRecipeId)))
}