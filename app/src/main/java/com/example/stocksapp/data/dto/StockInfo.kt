package com.example.stocksapp.data.dto

import com.example.stocksapp.data.local.StockInfoEntity
import com.google.gson.annotations.SerializedName

data class StockInfo(
    @SerializedName("Symbol") val symbol: String="",
    @SerializedName("AssetType") val assetType: String="",
    @SerializedName("Name") val name: String="",
    @SerializedName("Description") val description: String="",
    @SerializedName("Exchange") val exchange: String="",
    @SerializedName("Sector") val sector: String="",
    @SerializedName("Industry") val industry: String="",
    @SerializedName("MarketCapitalization") val marketCapitalization: Long=0,
    @SerializedName("PERatio") val peRatio: Double=0.0,
    @SerializedName("DividendYield") val dividendYield: Double=0.0,
    @SerializedName("ProfitMargin") val profitMargin: Double=0.0,
    @SerializedName("Beta") val beta: Double=0.0,
    @SerializedName("52WeekHigh") val fiftyTwoWeekHigh: Double=0.0,
    @SerializedName("52WeekLow") val fiftyTwoWeekLow: Double=0.0,
    var price:String="",
    var change_amount: String="",
    var change_percentage: String="",
)

fun StockInfo.toInternal() = StockInfoEntity(
    symbol = symbol,
    assetType = assetType,
    name = name,
    description = description,
    exchange = exchange,
    sector = sector,
    profitMargin = profitMargin,
    industry = industry,
    marketCapitalization = marketCapitalization,
    peRatio = peRatio,
    dividendYield = dividendYield,
    fiftyTwoWeekHigh = fiftyTwoWeekHigh,
    fiftyTwoWeekLow = fiftyTwoWeekLow,
    beta = beta
)

fun List<StockInfo>.toInternal() = map(StockInfo::toInternal)