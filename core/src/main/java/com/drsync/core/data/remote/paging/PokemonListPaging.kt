package com.drsync.core.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.drsync.core.data.remote.network.ApiService
import com.drsync.core.data.remote.response.Result

class PokemonListPaging(private val apiService: ApiService) : PagingSource<Int, Result>() {

    companion object {
        const val INITIAL_PAGE_OFFSET = 0
        const val INITIAL_PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val offset = params.key ?: INITIAL_PAGE_OFFSET
            val responseData = apiService.getPokemonList(INITIAL_PAGE_SIZE, offset).results

            LoadResult.Page(
                data = responseData as List<Result>,
                prevKey = if (offset == INITIAL_PAGE_OFFSET) null else offset - INITIAL_PAGE_SIZE,
                nextKey = if (responseData.isEmpty()) null else offset + INITIAL_PAGE_SIZE
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(INITIAL_PAGE_SIZE) ?: anchorPage?.nextKey?.minus(
                INITIAL_PAGE_SIZE
            )
        }
    }
}