package com.example.stocksapp.data.local

import androidx.room.TypeConverter
import com.example.stocksapp.data.dto.Stock
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    @TypeConverter
    fun fromTopStockList(stocks: List<Stock>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Stock>>() {}.type
        return gson.toJson(stocks, type)
    }

    @TypeConverter
    fun toTopStockList(stocksString: String): List<Stock> {
        val gson = Gson()
        val type = object : TypeToken<List<Stock>>() {}.type
        return gson.fromJson(stocksString, type)
    }

    @TypeConverter
    fun fromStockTimeSeriesList(stocks: List<StockTimeSeriesItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<StockTimeSeriesItem>>() {}.type
        return gson.toJson(stocks, type)
    }

    @TypeConverter
    fun toStockTimeSeriesList(stocksString: String): List<StockTimeSeriesItem> {
        val gson = Gson()
        val type = object : TypeToken<List<StockTimeSeriesItem>>() {}.type
        return gson.fromJson(stocksString, type)
    }

    @TypeConverter
    fun fromStockDataList(stocks: List<StockData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<StockData>>() {}.type
        return gson.toJson(stocks, type)
    }

    @TypeConverter
    fun toStockDataList(stocksString: String): List<StockData> {
        val gson = Gson()
        val type = object : TypeToken<List<StockData>>() {}.type
        return gson.fromJson(stocksString, type)
    }

}