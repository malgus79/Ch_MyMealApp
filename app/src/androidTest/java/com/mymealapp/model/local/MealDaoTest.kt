package com.mymealapp.model.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MealDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MealDataBase
    private lateinit var dao: MealDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MealDataBase::class.java
        )
            .allowMainThreadQueries().build()
        dao = database.mealDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSaveFavoriteMeal() = runTest {

        val favoritesEntity = FavoritesEntity(
            "52977",
            "Corba",
            "Side",
            "Turkish",
            "Pick through your lentils for any foreign debris, rinse them 2 or 3 times, drain, and set aside.  Fair warning, this will probably turn your lentils into a solid block that you’ll have to break up later\r\nIn a large pot over medium-high heat, sauté the olive oil and the onion with a pinch of salt for about 3 minutes, then add the carrots and cook for another 3 minutes.\r\nAdd the tomato paste and stir it around for around 1 minute. Now add the cumin, paprika, mint, thyme, black pepper, and red pepper as quickly as you can and stir for 10 seconds to bloom the spices. Congratulate yourself on how amazing your house now smells.\r\nImmediately add the lentils, water, broth, and salt. Bring the soup to a (gentle) boil.\r\nAfter it has come to a boil, reduce heat to medium-low, cover the pot halfway, and cook for 15-20 minutes or until the lentils have fallen apart and the carrots are completely cooked.\r\nAfter the soup has cooked and the lentils are tender, blend the soup either in a blender or simply use a hand blender to reach the consistency you desire. Taste for seasoning and add more salt if necessary.\r\nServe with crushed-up crackers, torn up bread, or something else to add some extra thickness.  You could also use a traditional thickener (like cornstarch or flour), but I prefer to add crackers for some texture and saltiness.  Makes great leftovers, stays good in the fridge for about a week.",
            "Soup",
            "https://www.youtube.com/watch?v=VVnZd8A84z4"
            )
        dao.saveFavoriteMeal(favoritesEntity)

        val meal = dao.getAllFavoritesMealsWithChanges().getOrAwaitValue()

        assertThat(meal).isNotEmpty()
        assertThat(meal).contains(favoritesEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAllFavoriteMeal() = runTest {

        val favoritesEntity = FavoritesEntity(
            "52977",
            "Corba",
            "Side",
            "Turkish",
            "Pick through your lentils for any foreign debris, rinse them 2 or 3 times, drain, and set aside.  Fair warning, this will probably turn your lentils into a solid block that you’ll have to break up later\r\nIn a large pot over medium-high heat, sauté the olive oil and the onion with a pinch of salt for about 3 minutes, then add the carrots and cook for another 3 minutes.\r\nAdd the tomato paste and stir it around for around 1 minute. Now add the cumin, paprika, mint, thyme, black pepper, and red pepper as quickly as you can and stir for 10 seconds to bloom the spices. Congratulate yourself on how amazing your house now smells.\r\nImmediately add the lentils, water, broth, and salt. Bring the soup to a (gentle) boil.\r\nAfter it has come to a boil, reduce heat to medium-low, cover the pot halfway, and cook for 15-20 minutes or until the lentils have fallen apart and the carrots are completely cooked.\r\nAfter the soup has cooked and the lentils are tender, blend the soup either in a blender or simply use a hand blender to reach the consistency you desire. Taste for seasoning and add more salt if necessary.\r\nServe with crushed-up crackers, torn up bread, or something else to add some extra thickness.  You could also use a traditional thickener (like cornstarch or flour), but I prefer to add crackers for some texture and saltiness.  Makes great leftovers, stays good in the fridge for about a week.",
            "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg",
            "Soup",
            "https://www.youtube.com/watch?v=VVnZd8A84z4"
        )
        val favoritesEntity2 = FavoritesEntity(
            "53060",
            "Burek",
            "Side",
            "Croatian",
            "Fry the finely chopped onions and minced meat in oil. Add the salt and pepper. Grease a round baking tray and put a layer of pastry in it. Cover with a thin layer of filling and cover this with another layer of filo pastry which must be well coated in oil. Put another layer of filling and cover with pastry. When you have five or six layers, cover with filo pastry, bake at 200ºC/392ºF for half an hour and cut in quarters and serve.",
            "https://www.themealdb.com/images/media/meals/tkxquw1628771028.jpg",
            "Streetfood, Onthego",
            "https://www.youtube.com/watch?v=YsJXZwE5pdY"
        )
        dao.saveFavoriteMeal(favoritesEntity)
        dao.saveFavoriteMeal(favoritesEntity2)

        val meal = dao.getAllFavoriteMeals()

        assertThat(meal).isNotEmpty()
        assertThat(meal.size).isEqualTo(2)
        assertThat(meal).contains(favoritesEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetMealById() = runTest {

        val favoritesEntity = FavoritesEntity(
            "52977",
            "Corba",
            "Side",
            "Turkish",
            "Pick through your lentils for any foreign debris, rinse them 2 or 3 times, drain, and set aside.  Fair warning, this will probably turn your lentils into a solid block that you’ll have to break up later\r\nIn a large pot over medium-high heat, sauté the olive oil and the onion with a pinch of salt for about 3 minutes, then add the carrots and cook for another 3 minutes.\r\nAdd the tomato paste and stir it around for around 1 minute. Now add the cumin, paprika, mint, thyme, black pepper, and red pepper as quickly as you can and stir for 10 seconds to bloom the spices. Congratulate yourself on how amazing your house now smells.\r\nImmediately add the lentils, water, broth, and salt. Bring the soup to a (gentle) boil.\r\nAfter it has come to a boil, reduce heat to medium-low, cover the pot halfway, and cook for 15-20 minutes or until the lentils have fallen apart and the carrots are completely cooked.\r\nAfter the soup has cooked and the lentils are tender, blend the soup either in a blender or simply use a hand blender to reach the consistency you desire. Taste for seasoning and add more salt if necessary.\r\nServe with crushed-up crackers, torn up bread, or something else to add some extra thickness.  You could also use a traditional thickener (like cornstarch or flour), but I prefer to add crackers for some texture and saltiness.  Makes great leftovers, stays good in the fridge for about a week.",
            "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg",
            "Soup",
            "https://www.youtube.com/watch?v=VVnZd8A84z4"
        )
        dao.saveFavoriteMeal(favoritesEntity)

        val meal = dao.getMealById(favoritesEntity.idMeal)

        assertThat(meal).isNotNull()
        assertThat(meal).isEqualTo(favoritesEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testDeleteFavoriteMeal() = runTest {
        val favoritesEntity = FavoritesEntity(
            "52977",
            "Corba",
            "Side",
            "Turkish",
            "Pick through your lentils for any foreign debris, rinse them 2 or 3 times, drain, and set aside.  Fair warning, this will probably turn your lentils into a solid block that you’ll have to break up later\r\nIn a large pot over medium-high heat, sauté the olive oil and the onion with a pinch of salt for about 3 minutes, then add the carrots and cook for another 3 minutes.\r\nAdd the tomato paste and stir it around for around 1 minute. Now add the cumin, paprika, mint, thyme, black pepper, and red pepper as quickly as you can and stir for 10 seconds to bloom the spices. Congratulate yourself on how amazing your house now smells.\r\nImmediately add the lentils, water, broth, and salt. Bring the soup to a (gentle) boil.\r\nAfter it has come to a boil, reduce heat to medium-low, cover the pot halfway, and cook for 15-20 minutes or until the lentils have fallen apart and the carrots are completely cooked.\r\nAfter the soup has cooked and the lentils are tender, blend the soup either in a blender or simply use a hand blender to reach the consistency you desire. Taste for seasoning and add more salt if necessary.\r\nServe with crushed-up crackers, torn up bread, or something else to add some extra thickness.  You could also use a traditional thickener (like cornstarch or flour), but I prefer to add crackers for some texture and saltiness.  Makes great leftovers, stays good in the fridge for about a week.",
            "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg",
            "Soup",
            "https://www.youtube.com/watch?v=VVnZd8A84z4"
        )
        dao.saveFavoriteMeal(favoritesEntity)
        dao.deleteFavoriteMeal(favoritesEntity)

        val meal = dao.getAllFavoritesMealsWithChanges().getOrAwaitValue()

        assertThat(meal).isEmpty()
        assertThat(meal).doesNotContain(favoritesEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSaveMeal() = runTest {
        val mealEntity = MealEntity(
            "52977",
            "Corba",
            "Side",
            "Turkish",
            "Pick through your lentils for any foreign debris, rinse them 2 or 3 times, drain, and set aside.  Fair warning, this will probably turn your lentils into a solid block that you’ll have to break up later\r\nIn a large pot over medium-high heat, sauté the olive oil and the onion with a pinch of salt for about 3 minutes, then add the carrots and cook for another 3 minutes.\r\nAdd the tomato paste and stir it around for around 1 minute. Now add the cumin, paprika, mint, thyme, black pepper, and red pepper as quickly as you can and stir for 10 seconds to bloom the spices. Congratulate yourself on how amazing your house now smells.\r\nImmediately add the lentils, water, broth, and salt. Bring the soup to a (gentle) boil.\r\nAfter it has come to a boil, reduce heat to medium-low, cover the pot halfway, and cook for 15-20 minutes or until the lentils have fallen apart and the carrots are completely cooked.\r\nAfter the soup has cooked and the lentils are tender, blend the soup either in a blender or simply use a hand blender to reach the consistency you desire. Taste for seasoning and add more salt if necessary.\r\nServe with crushed-up crackers, torn up bread, or something else to add some extra thickness.  You could also use a traditional thickener (like cornstarch or flour), but I prefer to add crackers for some texture and saltiness.  Makes great leftovers, stays good in the fridge for about a week.",
            "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg",
            "Soup",
            "https://www.youtube.com/watch?v=VVnZd8A84z4"
        )
        dao.saveMeal(mealEntity)

        val meal = dao.getMeals("")

        assertThat(meal).isNotEmpty()
    }

    @After
    fun tearDown() {
        database.close()
    }
}