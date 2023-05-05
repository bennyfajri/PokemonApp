package com.drsync.core.data.local.room

import androidx.room.*
import com.drsync.core.data.local.entity.PokemonEntity
import com.drsync.core.util.Constant.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getMyPokemonList(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun getMyPokemon(id: String): Flow<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Update
    suspend fun updatePokemon(pokemon: PokemonEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    suspend fun releasePokemon(id: String)

    @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE id = :id)")
    suspend fun isPokemonCatch(id: String): Boolean
}