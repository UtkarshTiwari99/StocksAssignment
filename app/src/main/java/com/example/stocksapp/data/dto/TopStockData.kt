package com.example.stocksapp.data.dto

 data class TopStockData(
  val top_gainers: List<Stock> = emptyList(),
  val top_losers: List<Stock> = emptyList()
 )