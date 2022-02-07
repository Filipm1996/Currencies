package com.example.currencies

import android.app.PendingIntent.getActivity
import android.service.autofill.Validators.not
import android.view.View
import android.widget.Button
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.currencies2.ListOfCurrenciesFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Matcher


@RunWith(AndroidJUnit4ClassRunner::class)
class UItest {


    @Test
    fun isRecyclerViewVisible(){
        val scenario : FragmentScenario<ListOfCurrenciesFragment> = launchFragmentInContainer (themeResId = R.style.AppTheme_NoActionBar)
        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.recyclerViewOfList)).check(matches(isDisplayed()))
    }

    @Test
    fun switchToMyCurrenciesFragment(){
        val scenario : FragmentScenario<ListOfCurrenciesFragment> = launchFragmentInContainer (themeResId = R.style.AppTheme_NoActionBar)
        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.bottomNav)).perform(NavigationViewActions.navigateTo(R.id.my_currencies))
        onView(withId(R.id.my_currencies)).check(matches(isDisplayed()))
    }

}

