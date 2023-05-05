package com.drsync.pokemonapp.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.drsync.core.data.PokemonRepository
import com.drsync.core.data.local.entity.PokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    fun getPokemonDetail(id: String) = repository.getPokemonDetail(id).asLiveData()

    fun insertPokemon(pokemonEntity: PokemonEntity) = viewModelScope.launch {
        repository.insertPokemon(pokemonEntity)
    }

    fun isPokemonCatch(id: String,isCatched: (Boolean) -> Unit) = viewModelScope.launch {
        isCatched(repository.isPokemonCatch(id))
    }

    fun releasePokemon(id: String) = viewModelScope.launch {
        repository.releasePokemon(id)
    }

    fun getMyPokemon(id: String) = repository.getMyPokemon(id).asLiveData()

    fun updatePokemon(pokemonEntity: PokemonEntity) = viewModelScope.launch {
        repository.updatePokemon(pokemonEntity)
    }
}