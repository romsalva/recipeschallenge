package com.romsalva.recipeschallenge.data.datasource.remote

import android.net.Uri
import com.romsalva.recipeschallenge.data.api.RecipesService
import com.romsalva.recipeschallenge.mockJsonRecipe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipesRemoteDataSourceTest {
    @MockK
    lateinit var service: RecipesService

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
        val recipes = listOf(mockJsonRecipe(17))
        coEvery { service.getRecipes() } returns recipes

        val result = RecipesRemoteDataSource(service).getAllRecipes()

        coVerify { service.getRecipes() }

        result.run {
            assertEquals(1, count())
            assertEquals(17, first().id)
        }
    }

}