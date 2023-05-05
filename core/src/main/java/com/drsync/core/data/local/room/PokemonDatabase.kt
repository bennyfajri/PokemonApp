package com.drsync.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drsync.core.data.local.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}