package com.example.stocksapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Query("SELECT * FROM top_stocks")
    fun getTopStocks(): Flow<List<TopStock>>

    @Query("SELECT * FROM stock_info WHERE symbol =:ticker")
    fun getStockInfo(ticker:String): Flow<StockData>

    @Query("SELECT MIN(creationTimeStamp) FROM stock_info where stockCategory=:stockCategory")
    suspend fun getOldestTimeStamp(stockCategory: String): Long?

    @Query("Select * From stock_info Where stockCategory=:stockCategory Order By page")
    fun getStockInfoPage(stockCategory: String): PagingSource<Int, StockData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: StockData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entities: List<StockData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: TopStock)

    @Upsert
    suspend fun upsertAllTopStocks(tasks: List<TopStock>)

    @Query("DELETE FROM stock_info WHERE symbol=:ticker")
    suspend fun deleteStockInfo(ticker: String)

    @Query("DELETE FROM top_stocks")
    suspend fun deleteTopStockData()

    @Query("Delete From stock_info where stockCategory=:stockCategory")
    suspend fun clearAllStockInfo(stockCategory: String)

}