package com.example.stocksapp.data.local

import androidx.room.Entity
import com.example.stocksapp.data.model.StockData

@Entity(
    tableName = "infra_data",primaryKeys = ["ticker", "timestamp"]
)
data class TimeSeriesEntity (
    val ticker:String,
    val timestamp: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long,
    val creationTimeStamp: Long = System.currentTimeMillis()
)


fun TimeSeriesEntity.toExternal() = StockData(
    ticker = ticker,
    timestamp = timestamp,
    open = open,
    high = high,
    low = low,
    close = close,
    volume = volume,
)

fun List<TimeSeriesEntity>.toExternal() = map(TimeSeriesEntity::toExternal)