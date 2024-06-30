package com.example.stocksapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stocksapp.data.dto.StockInfo

@Entity(
    tableName = "stock_info"
)
data class StockInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
    val symbol: String="",
    val assetType: String="",
    val name: String="",
    val description: String="",
    val exchange: String="",
    val sector: String="",
    val industry: String="",
    val marketCapitalization: Long=0,
    val peRatio: Double=0.0,
    val dividendYield: Double=0.0,
    val profitMargin: Double=0.0,
    val beta: Double=0.0,
    val fiftyTwoWeekHigh: Double=0.0,
    val fiftyTwoWeekLow: Double=0.0,
)

fun StockInfoEntity.toExternal() = StockInfo(
    symbol = symbol,
    assetType = assetType,
    name = name,
    description = description,
    exchange = exchange,
    sector = sector,
    industry = industry,
    marketCapitalization = marketCapitalization,
    peRatio = peRatio,
    dividendYield = dividendYield,
    profitMargin = profitMargin,
    beta = beta,
    fiftyTwoWeekHigh = fiftyTwoWeekHigh,
    fiftyTwoWeekLow = fiftyTwoWeekLow,
)

fun List<StockInfoEntity>.toExternal() = map(StockInfoEntity::toExternal)