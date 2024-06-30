package com.example.stocksapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.stocksapp.data.dto.StockInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE
import java.sql.Time

@Dao
interface StockDao {

    @Query("SELECT * FROM top_gainer_stocks")
    fun getAllTopGainer(): Flow<List<TopGainerStock>>

    @Query("SELECT * FROM stock_info WHERE symbol =:ticker")
    fun getStockInfo(ticker:String): Flow<StockInfoEntity>

    @Query("SELECT * FROM infra_data WHERE ticker= :ticker")
    fun getAllInfraData(ticker:String): Flow<List<TimeSeriesEntity>>

    @Query("DELETE FROM top_gainer_stocks")
    suspend fun deleteAllTopGainer()

    @Query("SELECT * FROM top_loser_stocks")
    fun getAllTopLoser(): Flow<List<TopLoserStock>>

    @Query("DELETE FROM top_loser_stocks")
    suspend fun deleteAllTopLoser()

    @Query("DELETE FROM stock_info WHERE symbol=:ticker")
    suspend fun deleteStockInfo(ticker: String)

    @Insert
    suspend fun insertOrUpdate(entity: StockInfoEntity)

    @Upsert
    suspend fun upsertAllTopLoser(tasks: List<TopLoserStock>)

    @Upsert
    suspend fun upsertAllTopGainer(tasks: List<TopGainerStock>)

    @Upsert
    suspend fun upsertAllTimeSeries(tasks: List<TimeSeriesEntity>)

}