package com.drsync.pokemonapp.mypokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.drsync.core.data.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    fun getMyPokemonList() = repository.getMyPokemonList().asLiveData()
}