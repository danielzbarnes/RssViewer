package com.danielzbarnes.rssviewer

import androidx.paging.PagingSource
import androidx.paging.PagingState

class RssListSource: PagingSource<Int, RssItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RssItem> {
        TODO("Not yet implemented")
    }

    override fun getRefreshKey(state: PagingState<Int, RssItem>): Int? {
        TODO("Not yet implemented")
    }
}