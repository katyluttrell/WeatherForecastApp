package com.katy.weatherforecastapp.ui.dialog

import android.app.AlertDialog
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katy.weatherforecastapp.MainActivity
import com.katy.weatherforecastapp.R
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
class AlertDialogFactoryUITest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var alertDialogFactory: AlertDialogFactory
    var testBoolean = false
    val callback = object : OnOkCallback {
        override fun onOkPress() {
            testBoolean = true
        }
    }
    lateinit var builder: AlertDialog.Builder

    lateinit var activity: ActivityScenario<MainActivity>
    @Before
    fun setup() {
        alertDialogFactory = AlertDialogFactory()
        testBoolean = false
        activity = ActivityScenario.launch(MainActivity::class.java)
        activity.onActivity {
            builder = AlertDialog.Builder(it)
            it.supportFragmentManager.popBackStackImmediate()
        }
    }
    @After
    fun teardown(){
        activity.close()
    }
    
    @Test
    fun testNoInternetNoDataDecoratorDialog() {
        val dialogType = MainViewDialog.NoInternetNoData
        var dialog: AlertDialog? = null
        activity.onActivity {
            dialog = alertDialogFactory.createDialog(builder, dialogType, callback)
            dialog?.show()
        }
        Thread.sleep(1000)
        assert(dialog?.isShowing == true)
        onView(withText(R.string.no_internet_no_data))
            .check(matches(isDisplayed()))
        onView(withText(R.string.try_again_with_internet))
            .check(matches(isDisplayed()))
        onView(withText(R.string.ok))
            .check(matches(isDisplayed()))
            .perform(click())
        assert(testBoolean)
        assert(dialog?.isShowing == false)
    }

    @Test
    fun testNetworkFetchErrorDecoratorDialog() {
        val dialogType = MainViewDialog.NetworkFetchError
        var dialog: AlertDialog? = null
        activity.onActivity {
            dialog = alertDialogFactory.createDialog(builder, dialogType, callback)
            dialog?.show()
        }
        Thread.sleep(1000)
        assert(dialog?.isShowing == true)
        onView(withText(R.string.network_fetch_error))
            .check(matches(isDisplayed()))
        onView(withText(R.string.please_try_again_later))
            .check(matches(isDisplayed()))
        onView(withText(R.string.ok))
            .check(matches(isDisplayed()))
            .perform(click())
        assert(testBoolean)
        assert(dialog?.isShowing == false)
    }

}