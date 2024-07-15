package com.example.stocksapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteDataKeys(
    @PrimaryKey(autoGenerate =false)
    @ColumnInfo(name = "stock_id")
    val stockTicker: String,
    @ColumnInfo(name = "prevKey")
    val prevKey: Int?,
    @ColumnInfo(name = "currentPage")
    val currentPage: Int,
    @ColumnInfo(name = "nextKey")
    val nextKey: Int?,
    val stockCategory:String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)