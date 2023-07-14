package com.katy.weatherforecastapp.model.remote

import com.katy.weatherforecastapp.util.testUtil.testObjects.TestObjectFactory
import org.junit.Assert
import org.junit.Test


internal class NetworkLocationTest {
    private val testObjectFactory = TestObjectFactory()
    @Test
    fun testAsExternalModule() {
        Assert.assertEquals(
            testObjectFactory.makeLocationObject(),
            testObjectFactory.makeNetworkLocationObject().asExternalModel("80303")
        )
    }
}