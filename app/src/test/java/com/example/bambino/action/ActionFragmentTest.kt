package com.example.bambino.action

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ActionFragmentTest {

    private lateinit var fragment: ActionFragment

    @Before
    fun setup() {
        fragment = ActionFragment()
    }

    @Test
    fun `empty action type returns false`() {
        val result = fragment.validateNewAction(
            System.currentTimeMillis(),
            "",
            3,
            "Something"
        )
        assertFalse(result)
    }

    @Test
    fun `wrong action type returns false`() {
        val result = fragment.validateNewAction(
            System.currentTimeMillis(),
            "asdasddas",
            3,
            "Something"
        )
        assertFalse(result)
    }

    @Test
    fun `empty description returns false`() {
        val result = fragment.validateNewAction(
            System.currentTimeMillis(),
            "Bath",
            3,
            ""
        )
        assertFalse(result)
    }

    @Test
    fun `humour bigger than 6 returns false`() {
        val result = fragment.validateNewAction(
            System.currentTimeMillis(),
            "Bath",
            7,
            "Something"
        )
        assertFalse(result)
    }

    @Test
    fun `humour less than 1 returns false`() {
        val result = fragment.validateNewAction(
            System.currentTimeMillis(),
            "Bath",
            0,
            "Something"
        )
        assertFalse(result)
    }

    @Test
    fun `action time less than 0 returns false`() {
        val result = fragment.validateNewAction(
            -1,
            "Bath",
            3,
            "Something"
        )
        assertFalse(result)
    }

    @Test
    fun `valid data returns true`() {
        val result = fragment.validateNewAction(
            System.currentTimeMillis(),
            "Bath",
            3,
            "Something"
        )
        assertTrue(result)
    }

}