package com.romsalva.recipeschallenge.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.romsalva.recipeschallenge.ui.composable.screen.RecipeListScreen
import com.romsalva.recipeschallenge.ui.stateholder.UiState

private const val recipeListRoute = "recipeList"

const val HomeRoute = recipeListRoute

fun NavGraphBuilder.recipeListScreen(
    onNavigateToDetail: (UiState.RecipeListItem) -> Unit
) {
    composable(recipeListRoute) { navBackStackEntry ->
        RecipeListScreen(viewModel = hiltViewModel(navBackStackEntry), onNavigateToDetail = onNavigateToDetail)
    }
}