package com.danielzbarnes.rssviewer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielzbarnes.rssviewer.api.RssFeedFetcher
import com.danielzbarnes.rssviewer.ui.theme.RssViewerTheme
import com.danielzbarnes.rssviewer.ui.theme.Shapes
import kotlinx.coroutines.*

private const val TAG = "RssMainActivity"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RssViewerTheme {

                Surface(color = MaterialTheme.colors.background) {

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {
                        Scaffold(topBar = { Header() },
                        content = { Rss() }
                        )
                    }
                }
            }
        }
    }
}

private suspend fun getRssList(): List<RssItem> =
    RssFeedFetcher().fetchRss()

@Composable
fun Rss() { // for handling the network call
    var rssList by remember { mutableStateOf(emptyList<RssItem>()) }
    LaunchedEffect(Unit) {
        rssList = getRssList()
    }
    RssList(rssList)
}

@Composable
fun Header() {
    TopAppBar(title = { Text("RSS Viewer") })
}


@Composable
fun RssList(list: List<RssItem>) { //list: List<RssItem>
    LazyColumn {
        items(list) { item ->
            RssItemCard(item)
        }
    }
}

@Composable
fun RssItemCard(item: RssItem) {

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = item.date, color = Color.Gray, fontSize = 16.sp)
            Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                text = item.summary,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RssViewerTheme {
        RssItemCard(RssItem("1", "Some Title", "Some summary text", "Aug 15, 2021"))
    }
}