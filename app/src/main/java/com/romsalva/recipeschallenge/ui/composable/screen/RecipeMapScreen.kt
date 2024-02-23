package com.romsalva.recipeschallenge.ui.composable.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.romsalva.recipeschallenge.R
import com.romsalva.recipeschallenge.ui.composable.RecipeMap
import com.romsalva.recipeschallenge.ui.stateholder.RecipeMapViewModel
import com.romsalva.recipeschallenge.ui.stateholder.UiState

@Composable
fun RecipeMapScreen(
    viewModel: RecipeMapViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val location by viewModel.recipeLocation.collectAsState()
    location?.let {
        RecipeMapScreen(
            recipeLocation = it,
            onNavigateUp = onNavigateUp,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeMapScreen(
    recipeLocation: UiState.RecipeLocation,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        RecipeMap(
            recipeLocation = recipeLocation,
            zoom = 12f
        )
        TopAppBar(
            title = { },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.background(Color.Transparent),
            navigationIcon = {
                IconButton(
                    onClick = { onNavigateUp() },
                    modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.desc_back))
                }
            })
    }
}

@Preview
@Composable
fun PreviewRecipeMapScreen() {
    Surface(color = MaterialTheme.colorScheme.surfaceDim) {
        RecipeMapScreen(
            recipeLocation = UiState.RecipeLocation(0, 10.10, -10.10),
            onNavigateUp = { }
        )
    }
}