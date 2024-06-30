package com.example.stocksapp.data.dto

import com.example.stocksapp.data.local.TopGainerStock
import com.example.stocksapp.data.local.TopLoserStock

data class Stock(
     val ticker: String="",
     val price: Double=0.0,
     val change_amount: Double=0.0,
     val change_percentage: String="",
     val volume: Long=0
 )

fun Stock.toInternalForGainer() = TopGainerStock(
    ticker = ticker,
    price = price,
    change_amount = change_amount,
    change_percentage = change_percentage,
    volume = volume
)


fun Stock.toInternalForLoser() = TopLoserStock(
    ticker = ticker,
    price = price,
    change_amount = change_amount,
    change_percentage = change_percentage,
    volume = volume
)

fun List<Stock>.toInternalForGainer() = map(Stock::toInternalForGainer)

fun List<Stock>.toInternalForLoser() = map(Stock::toInternalForLoser)