package com.example.stocksapp.data.local

data class StockTimeSeriesItem(
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long,
    val timestamp:String
)