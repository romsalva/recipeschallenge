package com.romsalva.recipeschallenge.ui.stateholder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romsalva.recipeschallenge.data.repository.DataResult
import com.romsalva.recipeschallenge.data.repository.RecipesRepository
import com.romsalva.recipeschallenge.ui.navigation.RecipeMapArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RecipeMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    recipesRepository: RecipesRepository
) : ViewModel() {

    private val args = RecipeMapArgs(savedStateHandle)

    val recipeLocation: StateFlow<UiState.RecipeLocation?> = recipesRepository.getRecipeById(args.recipeId)
        .map {
            when (it) {
                is DataResult.Success -> it.value.asUiStateRecipeLocation()
                is DataResult.Empty -> null
                is DataResult.Error -> null
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000),
            null
        )
}
