package com.drsync.core.data

import com.drsync.core.data.local.entity.PokemonEntity
import com.drsync.core.data.local.room.PokemonDao
import com.drsync.core.data.remote.RemoteDataSource
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private  val remoteData: RemoteDataSource,
    private val dao: PokemonDao
) {
    fun getPokemonList() = remoteData.getPokemonList()

    fun getPokemonDetail(id: String) = remoteData.getPokemonDetail(id)

    fun getMyPokemonList() = dao.getMyPokemonList()

    fun getMyPokemon(id: String) = dao.getMyPokemon(id)

    suspend fun insertPokemon(pokemonEntity: PokemonEntity) = dao.insertPokemon(pokemonEntity)

    suspend fun updatePokemon(pokemonEntity: PokemonEntity) = dao.updatePokemon(pokemonEntity)

    suspend fun releasePokemon(id: String) = dao.releasePokemon(id)

    suspend fun isPokemonCatch(id: String) = dao.isPokemonCatch(id)
}