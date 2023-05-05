package com.drsync.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class Result(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("url")
    val url: String
)
