package com.example.stocksapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["symbol"], unique = true)],
    tableName = "stock_info"
)
data class StockData(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    @ColumnInfo(defaultValue = "")
    val symbol: String="",
    @ColumnInfo(defaultValue = "")
    val assetType: String="",
    @ColumnInfo(defaultValue = "")
    val name: String="",
    @ColumnInfo(defaultValue = "")
    val description: String="",
    @ColumnInfo(defaultValue = "")
    val exchange: String="",
    @ColumnInfo(defaultValue = "")
    val sector: String="",
    @ColumnInfo(defaultValue = "")
    val industry: String="",
    @ColumnInfo(defaultValue = "0")
    val marketCapitalization: Long=0,
    @ColumnInfo(defaultValue = "0.0")
    val peRatio: Double=0.0,
    @ColumnInfo(defaultValue = "0.0")
    val dividendYield: Double=0.0,
    @ColumnInfo(defaultValue = "0.0")
    val profitMargin: Double=0.0,
    @ColumnInfo(defaultValue = "0.0")
    val beta: Double=0.0,
    @ColumnInfo(defaultValue = "0.0")
    val fiftyTwoWeekHigh: Double=0.0,
    @ColumnInfo(defaultValue = "0.0")
    val fiftyTwoWeekLow: Double=0.0,
    var stocks: List<StockTimeSeriesItem> = emptyList(),
    var creationTimeStamp: Long = System.currentTimeMillis(),
    var stockCategory:String=StockCategory.DEFAULT.stockCategory,
    @ColumnInfo(name = "page")
    var page: Int?=null,
){
    @Ignore
    var price:Double=0.0
    @Ignore
    var change_amount: Double=0.0
    @Ignore
    var change_percentage: String=""
}