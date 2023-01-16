package com.example.bambino.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.bambino.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ActionsDatabaseDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ActionsDatabase
    private lateinit var dao: ActionsDatabaseDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ActionsDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.actionsDatabaseDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAction() = runTest {
        val action = TrackedAction(
            1,
            System.currentTimeMillis(),
            "Bath",
            5,
            "Something"
        )
        dao.insert(action)

        val allActions = dao.getAllActions().getOrAwaitValue()

        assertThat(allActions).contains(action)
    }

    @Test
    fun deleteAction() = runTest {
        val action = TrackedAction(
            1,
            System.currentTimeMillis(),
            "Bath",
            5,
            "Something"
        )
        dao.insert(action)
        dao.deleteWithId(1)

        val allActions = dao.getAllActions().getOrAwaitValue()

        assertThat(allActions).doesNotContain(action)
    }

    @Test
    fun getActionsIfInTimeRange() = runTest {
        val action1 = TrackedAction(
            1,
            100,
            "Sleep",
            5,
            "Something"
        )
        val action2 = TrackedAction(
            2,
            500,
            "Sleep",
            5,
            "Something"
        )
        val action3 = TrackedAction(
            3,
            1000,
            "Sleep",
            5,
            "Something"
        )

        dao.insert(action1)
        dao.insert(action2)
        dao.insert(action3)

        val actionsFromRange = dao.getTodayActions(0, 600).getOrAwaitValue()

        assertThat(actionsFromRange).contains(action1)
        assertThat(actionsFromRange).contains(action2)
        assertThat(actionsFromRange).doesNotContain(action3)
    }

    @Test
    fun getActionById() = runTest {
        val action = TrackedAction(
            1,
            System.currentTimeMillis(),
            "Bath",
            5,
            "Something"
        )
        dao.insert(action)
        val sameAction = dao.get(1)

        assertThat(action).isEqualTo(sameAction)
    }

    @Test
    fun deleteIfInTimeRange() = runTest {
        val action1 = TrackedAction(
            1,
            100,
            "Sleep",
            5,
            "Something"
        )
        val action2 = TrackedAction(
            2,
            500,
            "Sleep",
            5,
            "Something"
        )
        val action3 = TrackedAction(
            3,
            1000,
            "Sleep",
            5,
            "Something"
        )

        dao.insert(action1)
        dao.insert(action2)
        dao.insert(action3)
        dao.clearDay(200, 800)

        val allActions = dao.getAllActions().getOrAwaitValue()

        assertThat(allActions).contains(action1)
        assertThat(allActions).doesNotContain(action2)
        assertThat(allActions).contains(action3)
    }
}