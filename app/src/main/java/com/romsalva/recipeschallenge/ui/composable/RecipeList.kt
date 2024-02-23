package com.romsalva.recipeschallenge.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.romsalva.recipeschallenge.ui.stateholder.UiState

@Composable
fun RecipeList(
    recipes: List<UiState.RecipeListItem>,
    onNavigateToDetail: (UiState.RecipeListItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recipes) { recipe ->
            RecipeListItem(
                recipe = recipe,
                onNavigateToDetail = onNavigateToDetail,
            )
        }
    }
}

@Preview
@Composable
fun PreviewRecipeList() {
    RecipeList(
        recipes = (0..20).map {
            UiState.RecipeListItem(
                it,
                "Recipe $it",
                "https://placehold.co/600x400".toUri()
            )
        },
        onNavigateToDetail = {}
    )
}
