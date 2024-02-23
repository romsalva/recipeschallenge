package com.romsalva.recipeschallenge.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.romsalva.recipeschallenge.R
import com.romsalva.recipeschallenge.ui.stateholder.UiState

@Composable
fun RecipeDetail(
    recipe: UiState.RecipeDetail,
    onNavigateToMap: (UiState.RecipeDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
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
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            OutlinedButton(
                onClick = { onNavigateToMap(recipe) },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.Bottom)
            ) {
                Image(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                Text(text = stringResource(R.string.origin))
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            recipe.ingredients.forEach {
                Text(
                    text = "• $it",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        Text(
            text = recipe.preparation,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}

@Preview
@Composable
fun PreviewRecipeDetail(@PreviewParameter(LoremIpsum::class) loremIpsum: String) {
    Surface {
        RecipeDetail(
            recipe = UiState.RecipeDetail(
                0,
                "Recipe Name",
                "https://placehold.co/600x400".toUri(),
                "Short Description",
                (1..7).map { "Ingredient $it" },
                loremIpsum
            ),
            onNavigateToMap = {}
        )
    }
}