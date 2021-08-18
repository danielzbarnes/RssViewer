package com.danielzbarnes.rssviewer.api

import com.danielzbarnes.rssviewer.RssItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


// Android 9(API 28) and above devices by default only allow secure connections using HTTPS, TLS, etc
// to enable connection using unencrypted HTTP protocol on Android 9+ devices
// network_security_config.xml needs to be defined with cleartextTrafficPermitted = "true"

// The app needs to then be told to use the defined network_security_config in the AndroidManifest.xml
// android:networkSecurityConfig="@xml/your_network_security_config" in the <application> tag
private const val RSS = "http://<YOUR RSS FEED HERE>.xml"

class RssFeedFetcher {

    private val rssFeedParser = RssFeedParser()

    @Suppress("BlockingMethodInNonBlockingContext")
    // This way of accessing the url generates an "Inappropriate blocking method call" Warning
    // because Android Studio registers the main thread is being blocked
    // however, this is incorrect because network call is being wrapped inside withContext(Dispatchers.IO)
    // the lint warning seems to be some issue with Android Studio

    suspend fun fetchRss(): List<RssItem> =
        withContext(Dispatchers.IO) {
            var rssList: List<RssItem> = emptyList()
            try {

                val url = URL(RSS)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                connection.apply {
                    readTimeout = 10000 // in millis
                    connectTimeout = 15000
                    requestMethod = "GET"
                    connect()
                }

                val input: InputStream = connection.inputStream

                rssList = rssFeedParser.parseRss(input) as ArrayList<RssItem>

                input.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext rssList
        }
}