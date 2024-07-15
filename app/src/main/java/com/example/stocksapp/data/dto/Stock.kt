package com.example.stocksapp.data.dto

data class Stock(
     val ticker: String="",
     val price: Double=0.0,
     val change_amount: Double=0.0,
     val change_percentage: String="",
     val volume: Long=0
 )