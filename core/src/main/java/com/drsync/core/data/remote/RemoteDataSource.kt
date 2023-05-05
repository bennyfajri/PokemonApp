package com.drsync.core.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.drsync.core.data.Resource
import com.drsync.core.data.remote.network.ApiService
import com.drsync.core.data.remote.paging.PokemonListPaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    fun getPokemonList(): Flow<PagingData<com.drsync.core.data.remote.response.Result>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                PokemonListPaging(apiService)
            }
        ).flow
    }

    fun getPokemonDetail(name: String) = flow {
        emit(Resource.Loading())
        val response = apiService.getPokemonDetail(name)
        emit(Resource.Success(response))
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}