package com.catastrophic.app.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.catastrophic.app.data.model.Cat
import com.catastrophic.app.data.source.ApiServiceCat
import javax.inject.Inject

class CatPagingSource @Inject constructor(
    private val apiServiceUser: ApiServiceCat
):PagingSource<Int,Cat>() {
    override fun getRefreshKey(state: PagingState<Int, Cat>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = apiServiceUser.getCatsImageAsync(page =  nextPageNumber)
            val data = LoadResult.Page(
                data = response.await(),
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
            data
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}