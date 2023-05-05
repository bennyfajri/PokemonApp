package com.drsync.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class VersionGroupDetail(
    @SerializedName("level_learned_at")
    val levelLearnedAt: Int,
    @SerializedName("version_group")
    val versionGroup: VersionGroup,
    @SerializedName("move_learn_method")
    val moveLearnMethod: MoveLearnMethod
)