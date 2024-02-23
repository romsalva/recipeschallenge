package com.romsalva.recipeschallenge.di

import android.content.Context
import androidx.room.Room
import com.romsalva.recipeschallenge.data.api.RecipesService
import com.romsalva.recipeschallenge.data.room.ChallengeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object ChallengeModule {

    @Singleton
    @Provides
    fun provideRecipesService(retrofit: Retrofit) =
        retrofit.create<RecipesService>()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://demo0898221.mockable.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            ChallengeDatabase::class.java,
            "challenge"
        ).build()

    @Singleton
    @Provides
    fun provideRecipesDao(database: ChallengeDatabase) =
        database.recipesDao()

}