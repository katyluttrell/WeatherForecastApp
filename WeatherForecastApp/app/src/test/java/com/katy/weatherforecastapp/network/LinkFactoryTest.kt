package com.katy.weatherforecastapp.network

import org.junit.Assert.assertEquals
import org.junit.Test


internal class LinkFactoryTest{
    @Test
    fun testOpenWeatherIconLink(){
        val expectedLink = "https://openweathermap.org/img/wn/10d@2x.png"
        val icon = "10d"
        val receivedLink = LinkFactory().openWeatherIconLink(icon)
        assertEquals(expectedLink, receivedLink)
    }
}