package com.katy.weatherforecastapp.ui

import androidx.fragment.app.activityViewModels
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.katy.weatherforecastapp.MainActivity
import com.katy.weatherforecastapp.R
import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import com.katy.weatherforecastapp.viewmodel.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainFragmentUiTest {

    val testObjectFactory = TestObjectFactory()
    val location = testObjectFactory.makeLocationObject()
    val weather = testObjectFactory.makeWeatherData5DayListInOrder()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    lateinit var activity: ActivityScenario<MainActivity>
    lateinit var viewModel: MainViewModel


    @Before
    fun setup() {
        hiltRule.inject()
        activity = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun teardown() {
        activity.close()
    }


    @Test
    fun dialogError() {
        onView(withId(R.id.enterZipCode)).check(matches(isDisplayed()))
            .check(matches(withText("Enter your zip code")))
        onView(withId(R.id.zipCodeTextField)).check(matches(isDisplayed()))
        onView(withText(R.string.ok)).check(matches(isClickable()))
            .perform(click())
        onView(withText("Invalid Zip Code")).check(matches(isDisplayed()))
    }

    @Test
    @LargeTest
    fun happyPath() {
        activity.onActivity {
            viewModel =
                it.supportFragmentManager.fragments[0].activityViewModels<MainViewModel>().value
        }
        //Dialog Success
        onView(withId(R.id.enterZipCode)).check(matches(isDisplayed()))
            .check(matches(withText("Enter your zip code")))
        onView(withHint("Zip Code")).check(matches(isDisplayed()))
            .perform(typeText("80303"))
        viewModel.location.postValue(location)
        onView(withText(R.string.ok)).check(matches(isClickable()))
            .perform(click())

        //Dialog gone
        onView(withText("Invalid Zip Code")).check(doesNotExist())
        onView(withId(R.id.enterZipCode)).check(doesNotExist())

        //Adding Weather data to VM
        viewModel.weatherDataList.postValue(weather)

        //View
        onView(withText("Boulder")).check(matches(isDisplayed()))
        onView(withId(R.id.editButton)).check(matches(isDisplayed())).check(matches(isClickable()))

    }
}