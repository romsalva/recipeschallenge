package com.romsalva.recipeschallenge.data.repository

import com.romsalva.recipeschallenge.data.datasource.local.RecipesLocalDataSource
import com.romsalva.recipeschallenge.data.datasource.remote.RecipesRemoteDataSource
import com.romsalva.recipeschallenge.data.model.Recipe
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
class RecipesRepository @Inject constructor(
    private val remoteDataSource: RecipesRemoteDataSource,
    private val localDataSource: RecipesLocalDataSource
) {

    fun getAllRecipes(): Flow<DataResult<List<Recipe>>> =
        localDataSource.getAllRecipes()
            .map {
                if (it.isEmpty())
                    DataResult.Empty()
                else
                    DataResult.Success(it)
            }
            .catch {
                emit(DataResult.Error.Database(it))
            }

    fun searchRecipesByNameOrIngredients(name: String): Flow<DataResult<List<Recipe>>> =
        localDataSource.searchRecipesByNameOrIngredients(name)
            .map {
                if (it.isEmpty())
                    DataResult.Empty()
                else
                    DataResult.Success(it)
            }
            .catch {
                emit(DataResult.Error.Database(it))
            }

    fun getRecipeById(id: Int): Flow<DataResult<Recipe>> =
        localDataSource.getRecipe(id)
            .map<Recipe, DataResult<Recipe>> {
                DataResult.Success(it)
            }
            .catch {
                emit(DataResult.Error.Database(it))
            }

    suspend fun fetch(): OperationResult {
        val result = try {
            remoteDataSource.getAllRecipes()
        } catch (exception: Exception) {
            return OperationResult.Error(exception)
        }
        try {
            localDataSource.saveAll(result)
        } catch (exception: Exception) {
            return OperationResult.Error(exception)
        }
        return OperationResult.Success
    }

}