package com.katy.weatherforecastapp.model.local

import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import org.junit.Assert
import org.junit.Test

internal class LocationEntityTest {
    private val testObjectFactory = TestObjectFactory()

    @Test
    fun testAsExternalModule() {
        Assert.assertEquals(
            testObjectFactory.makeLocationObject(),
            testObjectFactory.makeLocationEntityObject().asExternalModel()
        )
    }
}