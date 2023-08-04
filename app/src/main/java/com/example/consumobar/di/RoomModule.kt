package com.example.consumobar.di

import android.content.Context
import androidx.room.Room
import com.example.consumobar.data.database.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val BAR_DATABASE_NAME = "bar_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context : Context) = Room.databaseBuilder(context, ProductDatabase::class.java, BAR_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideProductDao(db : ProductDatabase) = db.getProductDao()
}