package com.mymealapp

import com.mymealapp.model.local.MealDaoTest
import com.mymealapp.model.local.MealDatabaseTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ExampleInstrumentedTest::class,
    MealDatabaseTest::class,
    MealDaoTest::class
)
class MealTestSuit