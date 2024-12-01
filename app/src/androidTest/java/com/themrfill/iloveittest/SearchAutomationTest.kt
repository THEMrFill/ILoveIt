package com.themrfill.iloveittest

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchAutomationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun simpleSearchIsVisibleTest() {
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun searchForFireTvTest() {
        with (composeTestRule) {
            onNodeWithText("Search").performTextInput("fire tv")
            onNodeWithContentDescription("Search now").performClick()
            waitForText("Search results")
            onNodeWithTag("Row 0").assertIsDisplayed()
        }
    }

    @Test
    fun productPageText() {
        with (composeTestRule) {
            onNodeWithText("Search").performTextInput("fire tv")
            onNodeWithContentDescription("Search now").performClick()
            waitForText("Search results")
            onNodeWithTag("Row 0").performClick()
            waitForIdle()
            onNodeWithText("Price").assertIsDisplayed()
        }
    }

    private fun waitForText(text: String) {
        val endTime = System.currentTimeMillis() + 10000L
        var exception: Throwable? = null
        while (System.currentTimeMillis() < endTime) {
            try {
                if (composeTestRule.onNode(hasText(text)).isDisplayed()) {
                    return
                }
            } catch (e: Throwable) {
                if (e !is ComposeTimeoutException) {
                    exception = e
                }
            }
        }
        if (exception != null) {
            throw RuntimeException(exception)
        }
    }
}