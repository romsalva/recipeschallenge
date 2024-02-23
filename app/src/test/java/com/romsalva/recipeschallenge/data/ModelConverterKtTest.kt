package com.romsalva.recipeschallenge.data

import android.net.Uri
import com.romsalva.recipeschallenge.data.api.model.JsonRecipe
import com.romsalva.recipeschallenge.data.model.LatLng
import com.romsalva.recipeschallenge.data.model.Recipe
import com.romsalva.recipeschallenge.data.room.entity.RoomIngredient
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipe
import com.romsalva.recipeschallenge.data.room.entity.RoomRecipeWithIngredients
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ModelConverterKtTest {

    lateinit var uri: Uri

    @Before
    fun setUp() {
        mockkStatic(Uri::class)
        uri = mockk()
        every { Uri.parse("http://placehold.co/600x400") } returns uri
    }

    @After
    fun tearDown() {
        unmockkStatic(Uri::class)
    }

    @Test
    fun `Recipe json to repo`() {
        JsonRecipe(
            1,
            "name",
            "description",
            listOf("ingredient"),
            "preparation",
            "http://placehold.co/600x400",
            10.10,
            -10.10
        )
            .toRecipe()
            .run {
                assertEquals(1, id)
                assertEquals("name", name)
                assertEquals("description", description)
                assertEquals(1, ingredients.count())
                assertEquals("ingredient", ingredients.first())
                assertEquals("preparation", preparation)
                assertEquals(uri, imageUri)
                assertEquals(10.10, origin.lat, 0.0001)
                assertEquals(-10.10, origin.lng, 0.0001)
            }
    }

    @Test
    fun `Recipe repo to room`() {
        every { uri.toString() } returns "http://placehold.co/600x400"
        Recipe(
            17,
            "name",
            "description",
            listOf("ingredient1", "ingredient2"),
            "preparation",
            mockk(),
            LatLng(10.10, -10.10)
        ).let {
            it.toRoomRecipe()
                .run {
                    assertEquals(17, id)
                    assertEquals("name", name)
                    assertEquals("description", description)
                    assertEquals("preparation", preparation)
                    assertEquals(10.10, lat, 0.0001)
                    assertEquals(-10.10, lng, 0.0001)
                }
            it.toRoomIngredients()
                .run {
                    assertEquals(2, count())
                    first().run {
                        assertEquals(0, order)
                        assertEquals(17, recipeId)
                        assertEquals("ingredient1", ingredient)
                    }
                }
        }
    }

    @Test
    fun `Recipe room to repo`() {
        RoomRecipeWithIngredients(
            RoomRecipe(
                17,
                "name",
                "description",
                "preparation",
                "http://placehold.co/600x400",
                10.10,
                -10.10
            ),
            listOf(
                RoomIngredient(
                    0,
                    17,
                    "ingredient"
                )
            )
        )
            .toRecipe()
            .run {
                assertEquals(17, id)
                assertEquals("name", name)
                assertEquals("description", description)
                assertEquals(1, ingredients.count())
                assertEquals("ingredient", ingredients.first())
                assertEquals("preparation", preparation)
                assertEquals(uri, imageUri)
                assertEquals(10.10, origin.lat, 0.0001)
                assertEquals(-10.10, origin.lng, 0.0001)
            }
    }
}