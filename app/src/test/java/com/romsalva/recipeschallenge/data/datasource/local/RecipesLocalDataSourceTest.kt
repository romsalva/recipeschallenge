package com.romsalva.recipeschallenge.data.datasource.local

import android.net.Uri
import com.romsalva.recipeschallenge.data.room.dao.RecipesDao
import com.romsalva.recipeschallenge.data.room.entity.RoomIngredient
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipe
import com.romsalva.recipeschallenge.mockRecipe
import com.romsalva.recipeschallenge.mockRoomRecipe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipesLocalDataSourceTest {
    @MockK
    lateinit var dao: RecipesDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("http://placehold.co/600x400") } returns uriMock
    }

    @After
    fun tearDown() {
        unmockkStatic(Uri::class)
    }

    @Test
    fun getAllRecipes() = runTest {
        val roomRecipes = listOf(mockRoomRecipe(17))
        every { dao.getAll() } returns flow {
            emit(roomRecipes)
        }

        val result = RecipesLocalDataSource(dao).getAllRecipes().first()

        verify(exactly = 1) { dao.getAll() }

        verify(exactly = 0) { dao.findByNameOrIngredients(any()) }
        verify(exactly = 0) { dao.findById(any()) }
        coVerify(exactly = 0) { dao.insertRecipe(any()) }
        coVerify(exactly = 0) { dao.insertIngredients(any()) }

        result.run {
            assertEquals(1, result.count())
            assertEquals(17, first().id)
        }
    }

    @Test
    fun searchRecipesByName() = runTest {
        val roomRecipes = listOf(mockRoomRecipe(17))
        every { dao.findByNameOrIngredients(any()) } returns flow {
            emit(roomRecipes)
        }

        val result = RecipesLocalDataSource(dao).searchRecipesByNameOrIngredients("name").first()

        verify(exactly = 1) { dao.findByNameOrIngredients("name") }

        verify(exactly = 0) { dao.getAll() }
        verify(exactly = 0) { dao.findById(any()) }
        coVerify(exactly = 0) { dao.insertRecipe(any()) }
        coVerify(exactly = 0) { dao.insertIngredients(any()) }

        result.run {
            assertEquals(1, result.count())
            assertEquals(17, first().id)
        }
    }

    @Test
    fun getRecipe() = runTest {
        val roomRecipe = mockRoomRecipe(17)
        every { dao.findById(17) } returns flow {
            emit(roomRecipe)
        }

        val result = RecipesLocalDataSource(dao).getRecipe(17).first()

        verify(exactly = 1) { dao.findById(17) }

        verify(exactly = 0) { dao.getAll() }
        verify(exactly = 0) { dao.findByNameOrIngredients(any()) }
        coVerify(exactly = 0) { dao.insertRecipe(any()) }
        coVerify(exactly = 0) { dao.insertIngredients(any()) }

        result.id
        assertEquals(17, result.id)
    }

    @Test
    fun saveAll() = runTest {
        val recipes = listOf(mockRecipe(17))
        coEvery { dao.insertRecipe(any()) } just runs
        coEvery { dao.insertIngredients(any()) } just runs

        RecipesLocalDataSource(dao).saveAll(recipes)

        val recipeSlot = slot<RoomRecipe>()
        val ingredientsSlot = slot<List<RoomIngredient>>()

        coVerify(exactly = 1) { dao.insertRecipe(capture(recipeSlot)) }
        coVerify(exactly = 1) { dao.insertIngredients(capture(ingredientsSlot)) }

        verify(exactly = 0) { dao.getAll() }
        verify(exactly = 0) { dao.findByNameOrIngredients(any()) }
        verify(exactly = 0) { dao.findById(any()) }

        recipeSlot.captured.run {
            assertEquals(17, id)
        }
        ingredientsSlot.captured.run {
            assertEquals(2, count())
        }
    }

}