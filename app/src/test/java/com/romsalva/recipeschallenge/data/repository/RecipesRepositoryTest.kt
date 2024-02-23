package com.romsalva.recipeschallenge.data.repository

import com.romsalva.recipeschallenge.data.datasource.local.RecipesLocalDataSource
import com.romsalva.recipeschallenge.data.datasource.remote.RecipesRemoteDataSource
import com.romsalva.recipeschallenge.data.model.Recipe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RecipesRepositoryTest {
    @MockK
    lateinit var remoteDataSource: RecipesRemoteDataSource

    @MockK
    lateinit var localDataSource: RecipesLocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `recipes success`() = runTest {
        val recipes = listOf<Recipe>(mockk())
        every { localDataSource.getAllRecipes() } returns flow {
            emit(recipes)
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).getAllRecipes().first()

        verify(exactly = 1) { localDataSource.getAllRecipes() }

        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients(any()) }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Success)
        (result as DataResult.Success).value.run {
            assertEquals(1, count())
        }
    }

    @Test
    fun `recipes empty`() = runTest {
        every { localDataSource.getAllRecipes() } returns flow {
            emit(emptyList())
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).getAllRecipes().first()

        verify(exactly = 1) { localDataSource.getAllRecipes() }

        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients(any()) }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Empty)
    }

    @Test
    fun `recipes error`() = runTest {
        every { localDataSource.getAllRecipes() } returns flow {
            throw Exception()
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).getAllRecipes().last()

        verify(exactly = 1) { localDataSource.getAllRecipes() }

        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients(any()) }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Error)
    }

    @Test
    fun `searchRecipesByNameOrIngredients success`() = runTest {
        val recipes = listOf<Recipe>(mockk())
        every { localDataSource.searchRecipesByNameOrIngredients(any()) } returns flow {
            emit(recipes)
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).searchRecipesByNameOrIngredients("query").first()

        verify(exactly = 1) { localDataSource.searchRecipesByNameOrIngredients("query") }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Success)
        (result as DataResult.Success).value.run {
            assertEquals(1, count())
        }
    }

    @Test
    fun `searchRecipesByNameOrIngredients empty`() = runTest {
        every { localDataSource.searchRecipesByNameOrIngredients(any()) } returns flow {
            emit(emptyList())
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).searchRecipesByNameOrIngredients("query").first()

        verify(exactly = 1) { localDataSource.searchRecipesByNameOrIngredients("query") }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Empty)
    }

    @Test
    fun `searchRecipesByNameOrIngredients error`() = runTest {
        every { localDataSource.searchRecipesByNameOrIngredients(any()) } returns flow {
            throw Exception()
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).searchRecipesByNameOrIngredients("query").first()

        verify(exactly = 1) { localDataSource.searchRecipesByNameOrIngredients("query") }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Error)
    }

    @Test
    fun `getRecipeById success`() = runTest {
        val recipe = mockk<Recipe>()
        every { localDataSource.getRecipe(1) } returns flow {
            emit(recipe)
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).getRecipeById(1).first()

        verify(exactly = 1) { localDataSource.getRecipe(1) }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients("query") }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Success)
        assertEquals(recipe, (result as DataResult.Success).value)
    }

    @Test
    fun `getRecipeById error`() = runTest {
        every { localDataSource.getRecipe(1) } returns flow {
            throw Exception()
        }

        val result = RecipesRepository(remoteDataSource, localDataSource).getRecipeById(1).first()

        verify(exactly = 1) { localDataSource.getRecipe(1) }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients("query") }
        coVerify(exactly = 0) { remoteDataSource.getAllRecipes() }

        assertTrue(result is DataResult.Error)
    }

    @Test
    fun `fetch success`() = runTest {
        val recipes = listOf<Recipe>(mockk())
        coEvery { remoteDataSource.getAllRecipes() } returns recipes
        coEvery { localDataSource.saveAll(any()) } just runs

        val result = RecipesRepository(remoteDataSource, localDataSource).fetch()

        coVerifySequence {
            remoteDataSource.getAllRecipes()
            localDataSource.saveAll(recipes)
        }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients("query") }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }

        assertTrue(result is OperationResult.Success)
    }

    @Test
    fun `fetch network error`() = runTest {
        coEvery { remoteDataSource.getAllRecipes() } throws Exception("network error")
        coEvery { localDataSource.saveAll(any()) } just runs

        val result = RecipesRepository(remoteDataSource, localDataSource).fetch()

        coVerify(exactly = 1) { remoteDataSource.getAllRecipes() }
        coVerify(exactly = 0) { localDataSource.saveAll(any()) }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients("query") }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }

        assertTrue(result is OperationResult.Error)
        assertEquals("network error", (result as OperationResult.Error).throwable.message)
    }

    @Test
    fun `fetch database error`() = runTest {
        val recipes = listOf<Recipe>(mockk())
        coEvery { remoteDataSource.getAllRecipes() } returns recipes
        coEvery { localDataSource.saveAll(any()) } throws Exception("database error")

        val result = RecipesRepository(remoteDataSource, localDataSource).fetch()

        coVerifySequence {
            remoteDataSource.getAllRecipes()
            localDataSource.saveAll(recipes)
        }

        verify(exactly = 0) { localDataSource.getAllRecipes() }
        verify(exactly = 0) { localDataSource.searchRecipesByNameOrIngredients("query") }
        verify(exactly = 0) { localDataSource.getRecipe(any()) }

        assertTrue(result is OperationResult.Error)
        assertEquals("database error", (result as OperationResult.Error).throwable.message)
    }

}