package com.romsalva.recipeschallenge.ui.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romsalva.recipeschallenge.data.repository.DataResult
import com.romsalva.recipeschallenge.data.repository.OperationResult
import com.romsalva.recipeschallenge.data.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val errorMessageLength = 3.seconds

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<UiState.Error>(UiState.Error.None)
    val error = _error.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val recipes: StateFlow<List<UiState.RecipeListItem>> = query
        .flatMapLatest { text ->
            if (text.isBlank()) {
                recipesRepository.getAllRecipes()
            } else {
                recipesRepository.searchRecipesByNameOrIngredients(text)
            }
        }.map { result ->
            when (result) {
                is DataResult.Success -> result.value.map { it.asUiStateRecipeListItem() }.also {
                    _error.value = UiState.Error.None
                }

                is DataResult.Empty -> emptyList<UiState.RecipeListItem>().also {
                    _error.value = UiState.Error.None
                }

                is DataResult.Error -> emptyList<UiState.RecipeListItem>().also {
                    _error.value = when (result) {
                        is DataResult.Error.Database -> UiState.Error.Database(result.message)
                        is DataResult.Error.Network -> UiState.Error.Network(result.message)
                        is DataResult.Error.Other -> UiState.Error.Other(result.message)
                    }
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000),
            emptyList()
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _loading.value = true
            recipesRepository.fetch().let { result ->
                if (result is OperationResult.Error) {
                    presentError(UiState.Error.Network(result.message))
                }
            }
            _loading.value = false
        }
    }

    fun search(text: String) {
        _query.value = text
    }

    private fun presentError(error: UiState.Error) {
        viewModelScope.launch {
            _error.value = error
            delay(errorMessageLength)
            _error.value = UiState.Error.None
        }
    }
}