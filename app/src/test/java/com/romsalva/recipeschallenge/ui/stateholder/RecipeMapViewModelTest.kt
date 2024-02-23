package com.romsalva.recipeschallenge.ui.stateholder

import androidx.lifecycle.SavedStateHandle
import com.romsalva.recipeschallenge.MainDispatcherRule
import com.romsalva.recipeschallenge.data.repository.DataResult
import com.romsalva.recipeschallenge.data.repository.RecipesRepository
import com.romsalva.recipeschallenge.mockRecipe
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeMapViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    @MockK
    lateinit var repository: RecipesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun recipeLocation() = runTest {
        val recipe = mockRecipe(17)
        every { savedStateHandle.get<Int>("recipeId") } returns 17
        every { repository.getRecipeById(17) } returns flow {
            emit(DataResult.Success(recipe))
        }

        val viewModel = RecipeMapViewModel(savedStateHandle, repository)
        advanceUntilIdle()
        val results = viewModel.recipeLocation.take(2).toList()
        advanceUntilIdle()

        verify(exactly = 1) { repository.getRecipeById(17) }

        verify(exactly = 0) { repository.getAllRecipes() }
        verify(exactly = 0) { repository.searchRecipesByNameOrIngredients(any()) }
        coVerify(exactly = 0) { repository.fetch() }

        assertNull(results[0])
        assertNotNull(results[1])
        results[1]?.run {
            assertEquals(17, id)
        }
    }
}