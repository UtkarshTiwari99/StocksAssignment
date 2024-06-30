package com.example.stocksapp.data.dto

import com.example.stocksapp.data.model.StockData
import com.google.gson.annotations.SerializedName

data class TimeSeriesData(
    @SerializedName("Time Series (5min)") val timeSeries: Map<String, StockDataDto>,
    @SerializedName("Meta Data") val metaData: Map<String, String>
)

data class StockDataDto(
    @SerializedName("1. open") val open: Double,
    @SerializedName("2. high") val high: Double,
    @SerializedName("3. low") val low: Double,
    @SerializedName("4. close") val close: Double,
    @SerializedName("5. volume") val volume: Long
)


fun TimeSeriesData.toExternal() = timeSeries.map { StockData(
        ticker= metaData["2. Symbol"]?:"",
        timestamp =it.key,
        open=it.value.open,
        high=it.value.high,
        low=it.value.low,
        close=it.value.close,
        volume=it.value.volume,
    )
 }.toList()