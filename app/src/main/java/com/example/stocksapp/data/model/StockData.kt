package com.example.stocksapp.data.model

import com.example.stocksapp.data.local.TimeSeriesEntity

data class StockData(
    val ticker: String,
    val timestamp: String,
    val open: Double,
    val high: Double,
    val low: Double, val close: Double,
    val volume: Long
)

fun StockData.toInternal() = TimeSeriesEntity(

    ticker = ticker,
    timestamp = timestamp,
    open = open,
    high = high,
    low = low, close = close,
    volume = volume
)

fun List<StockData>.toInternal() = map(StockData::toInternal)
