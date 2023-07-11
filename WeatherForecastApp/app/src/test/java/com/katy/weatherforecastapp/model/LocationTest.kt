package com.katy.weatherforecastapp.model

import com.katy.weatherforecastapp.testObjects.TestObjectFactory
import org.junit.Assert.assertEquals
import org.junit.Test


internal class LocationTest {

    private val testObjectFactory = TestObjectFactory()

    @Test
    fun testAsEntity() {
        assertEquals(
            testObjectFactory.makeLocationEntityObject(),
            testObjectFactory.makeLocationObject().asEntity()
        )
    }
}