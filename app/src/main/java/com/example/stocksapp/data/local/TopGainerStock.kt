package com.example.stocksapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stocksapp.data.dto.Stock

@Entity(
    tableName = "top_gainer_stocks"
)
data class TopGainerStock(
    @PrimaryKey val ticker: String,
    val price: Double,
    val change_amount: Double,
    val change_percentage: String,
    val volume: Long,
    val creationTimeStamp: Long = System.currentTimeMillis()
)

fun TopGainerStock.toExternal() = Stock(
    ticker = ticker,
    price = price,
    change_amount = change_amount,
    change_percentage = change_percentage,
    volume = volume
)

fun List<TopGainerStock>.toExternal() = map(TopGainerStock::toExternal)