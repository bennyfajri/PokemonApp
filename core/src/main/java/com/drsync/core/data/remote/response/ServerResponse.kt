package com.drsync.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ServerResponse (
    @field:SerializedName("count")
    val count: Int,

    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: String? = null,

    @field:SerializedName("results")
    val results: List<Result>? = null,
)