package com.lunamistica.app

import kotlin.test.Test
import kotlin.test.assertEquals

class ChakraVarietyTest {
    @Test
    fun everyChakraHasExtraAffirmationsAndPractices() {
        val names = Chakras.all.map { it.name }.toSet()
        assertEquals(names, Variety.chakraAffirmations.keys)
        assertEquals(names, Variety.chakraPractices.keys)
    }
}
