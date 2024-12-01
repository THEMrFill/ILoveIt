package com.themrfill.iloveittest.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class StringUtilsKtTest {

    @Test
    fun tidyTests() {
        assertEquals("no change", "no change".tidy())
        assertEquals("quotes here \" ", "quotes here &quot; ".tidy())
        assertEquals("amp here & ", "amp here &amp; ".tidy())
        assertEquals("both \" items & ", "both &quot; items &amp; ".tidy())
        assertEquals("swap & sides \" ", "swap &amp; sides &quot; ".tidy())
        assertEquals("multiple & amps & ", "multiple &amp; amps &amp; ".tidy())
        assertEquals("multiple \" quotes \" ", "multiple &quot; quotes &quot; ".tidy())
    }
}