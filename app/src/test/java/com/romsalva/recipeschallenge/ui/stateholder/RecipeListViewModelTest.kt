package com.romsalva.recipeschallenge.ui.stateholder

import com.romsalva.recipeschallenge.MainDispatcherRule
import com.romsalva.recipeschallenge.data.repository.DataResult
import com.romsalva.recipeschallenge.data.repository.OperationResult
import com.romsalva.recipeschallenge.data.repository.RecipesRepository
import com.romsalva.recipeschallenge.mockRecipe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var repository: RecipesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `recipes success`() = runTest {
        val recipes = listOf(mockRecipe(17))
        coEvery { repository.fetch() } returns OperationResult.Success
        every { repository.getAllRecipes() } returns flow {
            emit(DataResult.Success(recipes))
        }

        val viewModel = RecipeListViewModel(repository)
        advanceUntilIdle()
        val results = viewModel.recipes.take(2).toList()
        val error = viewModel.error.first()
        advanceUntilIdle()

        verify(exactly = 1) { repository.getAllRecipes() }
        coVerify(exactly = 1) { repository.fetch() }

        verify(exactly = 0) { repository.searchRecipesByNameOrIngredients(any()) }
        verify(exactly = 0) { repository.getRecipeById(any()) }

        assertEquals(0, results[0].count())
        assertEquals(1, results[1].count())
        results[1].first().run {
            assertEquals(17, id)
        }
        assertTrue(error is UiState.Error.None)
    }

    @Test
    fun `recipes empty`() = runTest {
        coEvery { repository.fetch() } returns OperationResult.Success
        every { repository.getAllRecipes() } returns flow {
            emit(DataResult.Empty())
        }

        val viewModel = RecipeListViewModel(repository)
        advanceUntilIdle()
        val result = viewModel.recipes.first()
        val error = viewModel.error.first()
        advanceUntilIdle()

        verify(exactly = 1) { repository.getAllRecipes() }
        coVerify(exactly = 1) { repository.fetch() }

        verify(exactly = 0) { repository.searchRecipesByNameOrIngredients(any()) }
        verify(exactly = 0) { repository.getRecipeById(any()) }

        assertEquals(0, result.count())
        assertTrue(error is UiState.Error.None)
    }

    @Test
    fun `recipes database error`() = runTest {
        coEvery { repository.fetch() } returns OperationResult.Success
        every { repository.getAllRecipes() } returns flow {
            emit(DataResult.Error.Database(Exception()))
        }

        val viewModel = RecipeListViewModel(repository)
        advanceUntilIdle()
        val result = viewModel.recipes.first()
        val errors = viewModel.error.take(2).toList()
        advanceUntilIdle()

        verify(exactly = 1) { repository.getAllRecipes() }
        coVerify(exactly = 1) { repository.fetch() }

        verify(exactly = 0) { repository.searchRecipesByNameOrIngredients(any()) }
        verify(exactly = 0) { repository.getRecipeById(any()) }

        assertEquals(0, result.count())
        assertTrue(errors[0] is UiState.Error.None)
        assertTrue(errors[1] is UiState.Error.Database)
    }

    @Test
    fun `recipes network error`() = runTest {
        coEvery { repository.fetch() } returns OperationResult.Error(Exception())

        val viewModel = RecipeListViewModel(repository)
        advanceUntilIdle()
        viewModel.refresh()
        val errors = viewModel.error.take(3).toList()
        advanceUntilIdle()

        coVerify(exactly = 2) { repository.fetch() }

        verify(exactly = 0) { repository.getAllRecipes() }
        verify(exactly = 0) { repository.searchRecipesByNameOrIngredients(any()) }
        verify(exactly = 0) { repository.getRecipeById(any()) }

        assertTrue(errors[0] is UiState.Error.None)
        assertTrue(errors[1] is UiState.Error.Network)
        assertTrue(errors[2] is UiState.Error.None)
    }

    @Test
    fun `recipes search success`() = runTest {
        val recipes = listOf(mockRecipe(17))
        coEvery { repository.fetch() } returns OperationResult.Success
        every { repository.searchRecipesByNameOrIngredients("text") } returns flow {
            emit(DataResult.Success(recipes))
        }

        val viewModel = RecipeListViewModel(repository)
        advanceUntilIdle()
        viewModel.search("text")
        advanceUntilIdle()
        val results = viewModel.recipes.take(2).toList()
        val error = viewModel.error.first()
        advanceUntilIdle()

        verify(exactly = 1) { repository.searchRecipesByNameOrIngredients("text") }
        coVerify(exactly = 1) { repository.fetch() }

        verify(exactly = 0) { repository.getAllRecipes() }
        verify(exactly = 0) { repository.getRecipeById(any()) }

        assertEquals(0, results[0].count())
        assertEquals(1, results[1].count())
        results[1].first().run {
            assertEquals(17, id)
        }
        assertTrue(error is UiState.Error.None)
    }

    @Test
    fun `recipes search empty`() = runTest {
        coEvery { repository.fetch() } returns OperationResult.Success
        every { repository.searchRecipesByNameOrIngredients("text") } returns flow {
            emit(DataResult.Empty())
        }

        val viewModel = RecipeListViewModel(repository)
        advanceUntilIdle()
        viewModel.search("text")
        advanceUntilIdle()
        val result = viewModel.recipes.first()
        val error = viewModel.error.first()
        advanceUntilIdle()

        verify(exactly = 1) { repository.searchRecipesByNameOrIngredients("text") }
        coVerify(exactly = 1) { repository.fetch() }

        verify(exactly = 0) { repository.getAllRecipes() }
        verify(exactly = 0) { repository.getRecipeById(any()) }

        assertEquals(0, result.count())
        assertTrue(error is UiState.Error.None)
    }

    @Test
    fun `recipes search database error`() = runTest {
        coEvery { repository.fetch() } returns OperationResult.Success
        every { repository.searchRecipesByNameOrIngredients("text") } returns flow {
            emit(DataResult.Error.Database(Exception()))
        }

        val viewModel = RecipeListViewModel(repository)
        advanceUntilIdle()
        viewModel.search("text")
        advanceUntilIdle()
        val result = viewModel.recipes.first()
        val errors = viewModel.error.take(2).toList()
        advanceUntilIdle()

        verify(exactly = 1) { repository.searchRecipesByNameOrIngredients("text") }
        coVerify(exactly = 1) { repository.fetch() }

        verify(exactly = 0) { repository.getAllRecipes() }
        verify(exactly = 0) { repository.getRecipeById(any()) }

        assertEquals(0, result.count())
        assertTrue(errors[0] is UiState.Error.None)
        assertTrue(errors[1] is UiState.Error.Database)
    }

    @Test
    fun `loading sequence`() = runTest {
        val recipes = listOf(mockRecipe(17))
        coEvery { repository.fetch() } returns OperationResult.Success
        every { repository.getAllRecipes() } returns flow {
            emit(DataResult.Success(recipes))
        }

        val viewModel = RecipeListViewModel(repository)
        val loadings = mutableListOf<Boolean>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.loading.take(3).toList(loadings)
        }
        viewModel.refresh()
        advanceUntilIdle()
        assertEquals(false, loadings[0])
        assertEquals(true, loadings[1])
        assertEquals(false, loadings[2])
    }
}