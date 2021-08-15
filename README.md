# RssViewer
A Simple app that displays the items from an RSS feed.  I wrote it for myself so I can easily verify an RSS feed I'm responsible for is updating.  Used it as an opportunity to play with Jetpack Compose.  LazyColumn is amazingly streamlined compared to RecyclerView.

This doesn't include a safety check on the connection status before doing the network call.  The network call method also needs to be updated for Android 8.