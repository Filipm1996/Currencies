package com.example.currencies


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.currencies.Activities_and_Fragments.MainActivity
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class UItest {

    @Test
    fun if_main_activity_is_visible(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.container)).check(matches(isDisplayed()))
    }

    @Test
    fun if_list_of_currencies_is_visible_after_starting(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.list_of_currencies)).check(matches(isDisplayed()))
    }

    @Test
    fun switching_from_list_of_currencies_to_my_currencies_fragment (){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.fragment_list_of_currencies)).check(matches(isDisplayed()))
        onView(withId(R.id.my_currencies)).perform(click())
        onView(withId(R.id.fragment_my_currencies)).check(matches(isDisplayed()))
    }

    @Test
    fun switching_from_list_of_currencies_to_list_of_cryptocurrencies (){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.fragment_list_of_currencies)).check(matches(isDisplayed()))
        onView(withId(R.id.list_of_cryptocurrencies)).perform(click())
        onView(withId(R.id.fragment_list_of_cryptocurrencies)).check(matches(isDisplayed()))
    }

    @Test
    fun switching_from_list_of_cryptocurrencies_to_my_currencies_fragment(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.fragment_list_of_currencies)).check(matches(isDisplayed()))
        onView(withId(R.id.list_of_cryptocurrencies)).perform(click())
        onView(withId(R.id.fragment_list_of_cryptocurrencies)).check(matches(isDisplayed()))
        onView(withId(R.id.my_currencies)).perform(click())
        onView(withId(R.id.fragment_my_currencies)).check(matches(isDisplayed()))
    }

}