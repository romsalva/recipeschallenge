package com.romsalva.recipeschallenge.ui.composable.screen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.romsalva.recipeschallenge.ui.navigation.HomeRoute
import com.romsalva.recipeschallenge.ui.navigation.navigateToRecipeDetail
import com.romsalva.recipeschallenge.ui.navigation.navigateToRecipeMap
import com.romsalva.recipeschallenge.ui.navigation.recipeDetailScreen
import com.romsalva.recipeschallenge.ui.navigation.recipeListScreen
import com.romsalva.recipeschallenge.ui.navigation.recipeMapScreen

@Composable
fun ChallengeApp() {
    val navController = rememberNavController()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = Modifier.fillMaxSize()
        ) {
            recipeListScreen(
                onNavigateToDetail = { navController.navigateToRecipeDetail(it.id) }
            )
            recipeDetailScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToMap = { navController.navigateToRecipeMap(it.id) }
            )
            recipeMapScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
