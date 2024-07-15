package com.example.stocksapp.data.dto

import com.google.gson.annotations.SerializedName

data class TimeSeriesData(
    @SerializedName("Meta Data") val metaData: Map<String, String>,
    @SerializedName("Time Series (5min)") val timeSeries: Map<String, StockDataDto>
)

data class StockDataDto(
    @SerializedName("1. open") val open: Double,
    @SerializedName("2. high") val high: Double,
    @SerializedName("3. low") val low: Double,
    @SerializedName("4. close") val close: Double,
    @SerializedName("5. volume") val volume: Long
)