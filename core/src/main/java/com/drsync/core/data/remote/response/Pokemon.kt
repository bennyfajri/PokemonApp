package com.drsync.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("base_experience")
    val baseExperience: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("is_default")
    val isDefault: Boolean,

    @SerializedName("order")
    val order: Int,

    @SerializedName("weight")
    val weight: Int,

    @SerializedName("abilities")
    val abilities: List<Ability>,

    @SerializedName("forms")
    val forms: List<Form>,

    @SerializedName("game_indices")
    val gameIndices: List<GameIndice>,

    @SerializedName("held_items")
    val heldItems: List<HeldItem>,

    @SerializedName("location_area_encounters")
    val locationAreaEncounters: String,

    @SerializedName("moves")
    val moves: List<Move>,

    @SerializedName("species")
    val species: Species,

    @SerializedName("sprites")
    val sprites: Sprites,

    @SerializedName("stats")
    val stats: List<Stat>,

    @SerializedName("types")
    val types: List<Type>
)