package com.katy.weatherforecastapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import org.junit.Test


internal class NetworkUtilsTest {

    @Test
    fun hasInternetAccessTrueWifiOnly() {
        val mockContext = mockk<Context>(relaxed = true)
        val mockConnectivityManager = mockk<ConnectivityManager>()
        val mockNetworkCapabilities = mockk<NetworkCapabilities>()
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { mockConnectivityManager.activeNetwork } returns mockk<Network>()
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns mockNetworkCapabilities
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        assert(NetworkUtils().hasInternetAccess(mockContext))
    }

    @Test
    fun hasInternetAccessTrueCellOnly() {
        val mockContext = mockk<Context>()
        val mockConnectivityManager = mockk<ConnectivityManager>()
        val mockNetworkCapabilities = mockk<NetworkCapabilities>()
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { mockConnectivityManager.activeNetwork } returns mockk<Network>()
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns mockNetworkCapabilities
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        assert(NetworkUtils().hasInternetAccess(mockContext))
    }

    @Test
    fun hasInternetAccessTrueWifiAndCell() {
        val mockContext = mockk<Context>()
        val mockConnectivityManager = mockk<ConnectivityManager>()
        val mockNetworkCapabilities = mockk<NetworkCapabilities>()
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { mockConnectivityManager.activeNetwork } returns mockk<Network>()
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns mockNetworkCapabilities
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        assert(NetworkUtils().hasInternetAccess(mockContext))
    }

    @Test
    fun hasInternetAccessFalseNoWifiNoCell() {
        val mockContext = mockk<Context>()
        val mockConnectivityManager = mockk<ConnectivityManager>()
        val mockNetworkCapabilities = mockk<NetworkCapabilities>()
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { mockConnectivityManager.activeNetwork } returns mockk<Network>()
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns mockNetworkCapabilities
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        assert(!NetworkUtils().hasInternetAccess(mockContext))
    }

    @Test
    fun hasInternetAccessFalseNetworkCapabilitiesNull() {
        val mockContext = mockk<Context>()
        val mockConnectivityManager = mockk<ConnectivityManager>()
        every { mockConnectivityManager.activeNetwork } returns mockk<Network>()
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns null
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        assert(!NetworkUtils().hasInternetAccess(mockContext))
    }

    @Test
    fun hasInternetAccessFalseNetworkNull() {
        val mockContext = mockk<Context>()
        val mockConnectivityManager = mockk<ConnectivityManager>()
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        every { mockConnectivityManager.activeNetwork } returns null
        assert(!NetworkUtils().hasInternetAccess(mockContext))
    }

}