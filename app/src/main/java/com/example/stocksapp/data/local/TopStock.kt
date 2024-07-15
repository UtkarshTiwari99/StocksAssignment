package com.example.stocksapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stocksapp.data.dto.Stock

@Entity(
    tableName = "top_stocks"
)
data class TopStock(
    @PrimaryKey
    val stockCategory:String,
    val stocks: List<Stock>,
    val creationTimeStamp: Long = System.currentTimeMillis(),
)

enum class StockCategory(val stockCategory: String) {
    TOP_GAINERS("Top Gainers"),
    TOP_LOSERS("Top Losers"),
    DEFAULT("Normal Stock")
}