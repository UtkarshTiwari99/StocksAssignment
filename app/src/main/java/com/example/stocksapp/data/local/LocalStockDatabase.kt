package com.example.stocksapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TopGainerStock::class, TopLoserStock::class, TimeSeriesEntity::class, StockInfoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalStockDatabase : RoomDatabase() {

    abstract fun stockDao(): StockDao

}
