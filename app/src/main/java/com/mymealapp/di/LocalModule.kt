package com.mymealapp.di

import android.content.Context
import androidx.room.Room
import com.mymealapp.core.Constants.MEAL_DATABASE_NAME
import com.mymealapp.model.local.MealDao
import com.mymealapp.model.local.MealDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context): MealDataBase {
        return Room.databaseBuilder(context, MealDataBase::class.java, MEAL_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesMealDao(mealDB: MealDataBase): MealDao {
        return mealDB.mealDao()
    }
}