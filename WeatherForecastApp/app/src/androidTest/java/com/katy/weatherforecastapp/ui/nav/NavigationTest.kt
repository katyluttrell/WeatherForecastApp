package com.katy.weatherforecastapp.ui.nav

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.launchFragmentInHiltContainer
import com.katy.weatherforecastapp.ui.MainFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()

    }

    @Test
    fun testZipCodeDialogFragmentNavigation() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            navController.setGraph(R.navigation.nav_graph)
            launchFragmentInHiltContainer<MainFragment> {
                Navigation.setViewNavController(this.requireView(), navController)
                onView(withId(R.id.mainFragment)).check(matches(isDisplayed()))
                onView(withId(R.id.enterZipCode)).check(matches(isDisplayed()))
            }

    }

}