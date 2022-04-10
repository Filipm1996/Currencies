package com.example.currencies


import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.currencies.Activities_and_Fragments.MainActivity
import com.example.currencies.Activities_and_Fragments.MyCurrenciesFragment
import com.example.currencies.Activities_and_Fragments.ListOfCurrenciesFragment

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class UItest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isRecyclerViewVisibleInListOfCurrenciesFragment(){
        val scenario : FragmentScenario<ListOfCurrenciesFragment> = launchFragmentInContainer (themeResId = R.style.AppTheme_NoActionBar)
        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.recyclerViewOfList)).check(matches(isDisplayed()))
    }

    @Test
    fun isRecyclerViewVisibleInMyCurrenciesFragment(){
        val scenario : FragmentScenario<MyCurrenciesFragment> = launchFragmentInContainer (themeResId = R.style.AppTheme_NoActionBar)
        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.recyclerViewOfList)).check(matches(isDisplayed()))
    }
}

