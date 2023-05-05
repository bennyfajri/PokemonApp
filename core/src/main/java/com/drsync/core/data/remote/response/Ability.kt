package com.drsync.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class Ability(
    @SerializedName("is_hidden")
    val isHidden: Boolean,

    @SerializedName("slot")
    val slot: Int,

    @SerializedName("ability")
    val ability: AbilityX
)