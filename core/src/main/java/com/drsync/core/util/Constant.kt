package com.drsync.core.util

import com.drsync.core.data.remote.response.Result

object Constant {
    const val TABLE_NAME = "MyPokemon"

    val String.listPokemonImageUrl get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${this}.png"

    val Result.getId: String get() {
        val path = url.split("/")
        return path[path.size - 2]
    }

    val String.getId: String get() {
        val path = this.split("/")
        return path[path.size - 2]
    }
}