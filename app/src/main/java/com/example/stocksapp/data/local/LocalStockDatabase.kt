package com.example.stocksapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ TopStock::class, StockData::class, RemoteDataKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class LocalStockDatabase : RoomDatabase() {

    abstract fun stockDao(): StockDao

    abstract fun remoteKeyDao():RemoteKeysDao

}