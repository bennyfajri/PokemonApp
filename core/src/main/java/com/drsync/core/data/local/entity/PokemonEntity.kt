package com.drsync.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drsync.core.util.Constant.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class PokemonEntity(
    @field:ColumnInfo(name = "name")
    val name: String,

    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: String,

    @field:ColumnInfo(name = "renamed")
    val renamed: Int? = 0,
)