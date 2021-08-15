package com.danielzbarnes.rssviewer.api

import android.util.Log
import com.danielzbarnes.rssviewer.RssItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "RssFeedParser"

class RssFeedParser {

    fun parseRss(input: InputStream): List<RssItem>{

        val rssList = ArrayList<RssItem>()

        try {
            var rssItem = RssItem()
            var tagname: String?
            var text = ""
            var depth: Int

            val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true

            val parser: XmlPullParser = factory.newPullParser()

            parser.apply {
                setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                setInput(input, null)
            }

            var event = parser.eventType

            while (event != XmlPullParser.END_DOCUMENT){

                tagname = parser.name
                depth = parser.depth

                // this will require additional logic to handle TTT RSS if TTT is added as bottom nav option
                when (event){
                    XmlPullParser.START_TAG -> if (tagname == "item") rssItem = RssItem()
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> when (tagname) {
                        "title" -> if (depth == 3) Log.d(TAG, "Begin parsing RSS Feed: $text")
                        else if (depth == 4) rssItem.title = text

                        "pubDate" -> rssItem.date = parsePubDate(text)

                        "guid" -> rssItem.id = parseGuid(text)

                        "itunes:summary" -> if (depth == 4) rssItem.summary = text
                        "item" -> rssList.add(rssItem)
                    }
                }
                event = parser.next()
            }
        } catch (e: Exception) { e.printStackTrace() }
        catch (e: XmlPullParserException) { e.printStackTrace() }

        return rssList
    }

    // returns the date in the format of Jan, 01, 2021
    private fun parsePubDate(date: String): String{

        val rssDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
        val dateObj = rssDateFormat.parse(date) // a date object to handle reformatting
        val uiFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH) // the format for the ui

        return uiFormat.format(dateObj!!)
    }

    // returns the id from the guid url
    private fun parseGuid(url: String): String {

        // the '=' is the delimiter where the id starts
        val equalsIndex = url.indexOf("=", 0, false)
        // everything following the '=' delimiter to the end of the string is the id
        return if ( equalsIndex != -1) url.slice(IntRange(equalsIndex+1, url.length-1)) else ""
    }
}