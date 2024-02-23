package com.romsalva.recipeschallenge.ui.composable.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.core.net.toUri
import com.romsalva.recipeschallenge.R
import com.romsalva.recipeschallenge.ui.composable.RecipeDetail
import com.romsalva.recipeschallenge.ui.stateholder.RecipeDetailViewModel
import com.romsalva.recipeschallenge.ui.stateholder.UiState

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel,
    onNavigateUp: () -> Unit,
    onNavigateToMap: (UiState.RecipeDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    val recipe by viewModel.recipeDetail.collectAsState()
    recipe?.let {
        RecipeDetailScreen(it, onNavigateUp, onNavigateToMap, modifier)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipe: UiState.RecipeDetail,
    onNavigateUp: () -> Unit,
    onNavigateToMap: (UiState.RecipeDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.recipe)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.desc_back))
                    }
                })
        },
        modifier = modifier
    ) { innerPadding ->
        RecipeDetail(
            recipe = recipe,
            onNavigateToMap = onNavigateToMap,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview
@Composable
fun PreviewRecipeDetailScreen(@PreviewParameter(LoremIpsum::class) loremIpsum: String) {
    RecipeDetailScreen(
        UiState.RecipeDetail(
            0,
            "Recipe 0",
            "https://placehold.co/600x400".toUri(),
            "Short description",
            (1..7).map { "Ingredient $it" },
            loremIpsum
        ),
        onNavigateUp = {},
        onNavigateToMap = {}
    )
}