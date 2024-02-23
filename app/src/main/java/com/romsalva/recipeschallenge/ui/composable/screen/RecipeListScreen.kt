package com.romsalva.recipeschallenge.ui.composable.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.romsalva.recipeschallenge.R
import com.romsalva.recipeschallenge.ui.composable.RecipeList
import com.romsalva.recipeschallenge.ui.stateholder.RecipeListViewModel
import com.romsalva.recipeschallenge.ui.stateholder.UiState

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel,
    onNavigateToDetail: (UiState.RecipeListItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val recipes by viewModel.recipes.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val searchQuery by viewModel.query.collectAsState()
    val error by viewModel.error.collectAsState()
    var searchOpen by remember { mutableStateOf(false) }
    RecipeListScreen(
        recipes = recipes,
        onNavigateToDetail = onNavigateToDetail,
        loading = loading,
        searchOpen = searchOpen,
        searchQuery = searchQuery,
        error = error,
        onSearchOpenChanged = { searchOpen = it },
        onSearchQueryChanged = { viewModel.search(it) },
        onRefresh = {
            viewModel.refresh()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    recipes: List<UiState.RecipeListItem>,
    onNavigateToDetail: (UiState.RecipeListItem) -> Unit,
    loading: Boolean,
    searchOpen: Boolean,
    searchQuery: String,
    error: UiState.Error,
    onSearchOpenChanged: (Boolean) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.recipes)) },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        AnimatedVisibility(
                            visible = !searchOpen && searchQuery.isNotBlank(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            InputChip(
                                selected = false,
                                onClick = { onSearchOpenChanged(true) },
                                label = { Text(text = searchQuery) },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { onSearchQueryChanged("") },
                                        modifier = Modifier.size(InputChipDefaults.IconSize)
                                    ) {
                                        Icon(Icons.Outlined.Clear, stringResource(R.string.desc_clear_filter))
                                    }
                                },
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }
                        AnimatedVisibility(
                            visible = searchOpen || searchQuery.isBlank(),
                            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
                        ) {
                            IconButton(onClick = { onSearchOpenChanged(!searchOpen) }) {
                                Icon(Icons.Outlined.Search, stringResource(R.string.desc_search))
                            }
                        }
                        AnimatedVisibility(
                            visible = loading,
                            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
                            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(end = 12.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        }
                        AnimatedVisibility(
                            visible = !loading,
                            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
                        ) {
                            IconButton(
                                onClick = { onRefresh() }
                            ) {
                                Icon(Icons.Outlined.Refresh, stringResource(R.string.refresh))
                            }
                        }
                    }
                )
                AnimatedVisibility(visible = searchOpen) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { onSearchQueryChanged(it) },
                        onSearch = { onSearchOpenChanged(false) },
                        active = false,
                        onActiveChange = { },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    onSearchQueryChanged("")
                                    onSearchOpenChanged(false)
                                }
                            ) {
                                Icon(Icons.Outlined.Close, stringResource(R.string.desc_close))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {}
                }
                AnimatedVisibility(visible = error !is UiState.Error.None) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(32.dp, 128.dp)
                    ) {
                        val text = when (error) {
                            is UiState.Error.Database -> stringResource(R.string.database_error, error.message)
                            is UiState.Error.Network -> stringResource(R.string.network_error, error.message)
                            is UiState.Error.Other -> stringResource(R.string.generic_error, error.message)
                            is UiState.Error.None -> ""
                        }
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .then(modifier)
    ) { innerPadding ->
        RecipeList(
            recipes = recipes,
            onNavigateToDetail = onNavigateToDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }

}

@Preview
@Composable
fun PreviewRecipeListScreen() {
    RecipeListScreen(
        recipes = (0..20).map {
            UiState.RecipeListItem(
                it,
                "Recipe $it",
                "https://placehold.co/600x400".toUri()
            )
        },
        loading = false,
        searchOpen = false,
        searchQuery = "",
        error = UiState.Error.None,
        onSearchOpenChanged = {},
        onNavigateToDetail = {},
        onRefresh = {},
        onSearchQueryChanged = {}
    )
}

@Preview
@Composable
fun PreviewRecipeListScreenSearchClosed() {
    RecipeListScreen(
        recipes = (0..20).map {
            UiState.RecipeListItem(
                it,
                "Recipe $it",
                "https://placehold.co/600x400".toUri()
            )
        },
        loading = true,
        searchOpen = false,
        searchQuery = "some search",
        error = UiState.Error.None,
        onSearchOpenChanged = {},
        onNavigateToDetail = {},
        onRefresh = {},
        onSearchQueryChanged = {}
    )
}

@Preview
@Composable
fun PreviewRecipeListScreenSearchOpen() {
    RecipeListScreen(
        recipes = (0..20).map {
            UiState.RecipeListItem(
                it,
                "Recipe $it",
                "https://placehold.co/600x400".toUri()
            )
        },
        loading = true,
        searchOpen = true,
        searchQuery = "some search",
        error = UiState.Error.None,
        onSearchOpenChanged = {},
        onNavigateToDetail = {},
        onRefresh = {},
        onSearchQueryChanged = {}
    )
}

@Preview
@Composable
fun PreviewRecipeListScreenError() {
    RecipeListScreen(
        recipes = (0..20).map {
            UiState.RecipeListItem(
                it,
                "Recipe $it",
                "https://placehold.co/600x400".toUri()
            )
        },
        loading = false,
        searchOpen = false,
        searchQuery = "",
        error = UiState.Error.Database("Dummy"),
        onSearchOpenChanged = {},
        onNavigateToDetail = {},
        onRefresh = {},
        onSearchQueryChanged = {}
    )
}