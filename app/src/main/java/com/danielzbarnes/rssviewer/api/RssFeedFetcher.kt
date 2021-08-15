package com.danielzbarnes.rssviewer.api

import com.danielzbarnes.rssviewer.RssItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


private const val RSS = "http://<YOUR RSS FEED HERE>.xml"

class RssFeedFetcher {

    private val rssFeedParser = RssFeedParser()

    suspend fun fetchRss(): List<RssItem> =
        withContext(Dispatchers.IO) {
            var rssList: List<RssItem> = emptyList()
            try {

                // this method of network call is functional but needs to be updated for Android 8
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