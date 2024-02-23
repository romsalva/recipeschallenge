package com.romsalva.recipeschallenge.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.romsalva.recipeschallenge.ui.stateholder.UiState

@Composable
fun RecipeListItem(
    recipe: UiState.RecipeListItem,
    onNavigateToDetail: (UiState.RecipeListItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onNavigateToDetail(recipe) },
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        ) {
            AsyncImage(
                model = recipe.imageUri,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(96.dp, 192.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewRecipeListItem(@PreviewParameter(LoremIpsum::class) loremIpsum: String) {
    RecipeListItem(
        recipe = UiState.RecipeListItem(
            0,
            "Recipe Name",
            "https://placehold.co/600x400".toUri()
        ),
        onNavigateToDetail = {}
    )
}