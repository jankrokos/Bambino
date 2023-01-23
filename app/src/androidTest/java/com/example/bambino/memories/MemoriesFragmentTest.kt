package com.example.bambino.memories

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.example.bambino.MainActivity
import com.example.bambino.MyViewAction.clickChildViewWithId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.bambino.R
import org.junit.Before

@RunWith(AndroidJUnit4::class)
@LargeTest
class MemoriesFragmentTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        activityScenario = launch(MainActivity::class.java)
    }

    @Test
    fun navigateToNewMemory() {
        onView(withId(R.id.go_to_memo_add))
            .perform(scrollTo(), click())

        onView(withId(R.id.new_memory_photo))
            .check(matches(isDisplayed()))
    }

    @Test
    fun navigateToTrackedActions() {
        onView(withId(R.id.go_to_actions_list))
            .perform(click())

        onView(withId(R.id.chosen_date_text_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun addNewAction() {
        onView(withId(R.id.go_to_actions_add))
            .perform(click())

        onView(withId(R.id.description_action))
            .perform(click(), typeText("Something"), closeSoftKeyboard())

        onView(withId(R.id.add_activity_button))
            .perform(click())

        onView(withId(R.id.welcome_back))
            .check(matches(isDisplayed()))

    }

    @Test
    fun editMemoryPhoto() {
        onView(withId(R.id.go_to_memo_list))
            .perform(scrollTo(), click())

        onView(withId(R.id.memories_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.edit_memory_button)
                )
            )

        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).run {
            onView(withId(R.id.new_memory_photo))
                .perform(click())
            waitForIdle()
//            click(displayWidth / 2, displayHeight / 2)
            pressBack()
        }

        onView(withId(R.id.confirm_edit_memory_button))
            .perform(click())

        onView(withId(R.id.add_memory_entry_button))
            .check(matches(isDisplayed()))

    }

    @Test
    fun editMemory() {
        onView(withId(R.id.go_to_memo_list))
            .perform(scrollTo(), click())

        onView(withId(R.id.memories_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    clickChildViewWithId(R.id.edit_memory_button)
                )
            )

        val randomDescription = (0..10000).random().toString()

        onView(withId(R.id.memo_description_here))
            .perform(click(), replaceText(randomDescription), closeSoftKeyboard())

        onView(withId(R.id.confirm_edit_memory_button))
            .perform(click())

        onView(withId(R.id.add_memory_entry_button))
            .check(matches(isDisplayed()))
    }

}