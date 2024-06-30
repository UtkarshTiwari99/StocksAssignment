package com.example.stocksapp.data.repository

import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.dto.TopStockData
import com.example.stocksapp.data.dto.toInternalForGainer
import com.example.stocksapp.data.dto.toInternalForLoser
import com.example.stocksapp.data.local.StockDao
import com.example.stocksapp.data.local.toExternal
import com.example.stocksapp.data.remote.NetworkResult
import com.example.stocksapp.data.remote.RemoteDataSource
import com.example.stocksapp.data.remote.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
import com.example.stocksapp.data.dto.StockInfo
import com.example.stocksapp.data.dto.toInternal
import com.example.stocksapp.data.local.StockInfoEntity
import com.example.stocksapp.data.model.StockData
import com.example.stocksapp.data.model.toInternal
import com.example.stocksapp.data.remote.Loading
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform

@Singleton
class StockRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val stockLocalDataSource: StockDao) {

    fun observeAllTopGainer() : Flow<List<Stock>> {
       return stockLocalDataSource.getAllTopGainer().map { it.toExternal() }
    }

    fun observeStockInfo(ticker: String): Flow<StockInfo> {
        return stockLocalDataSource.getStockInfo(ticker).map { it.toExternal() }
    }

    fun observeInfraDayData(ticker:String) : Flow<List<StockData>> {
      return stockLocalDataSource.getAllInfraData(ticker).map { it.toExternal() }
    }

    fun observeAllTopLoser() : Flow<List<Stock>> {
        return stockLocalDataSource.getAllTopLoser().map { it.toExternal() }
    }

    suspend fun refreshTopLoser(): NetworkResult<TopStockData> {
        val networkTasks = remoteDataSource.getTopStocks()
        if (networkTasks is Success) {
            stockLocalDataSource.deleteAllTopGainer()
            stockLocalDataSource.deleteAllTopLoser()
            val localTasks = withContext(Dispatchers.IO) {
                networkTasks
            }
            stockLocalDataSource.upsertAllTopGainer(networkTasks.data.top_gainers.toInternalForGainer())
            stockLocalDataSource.upsertAllTopLoser(networkTasks.data.top_losers.toInternalForLoser())
            return networkTasks
        }
        return networkTasks
    }

    suspend fun load(): NetworkResult<TopStockData> {
        val networkTasks = remoteDataSource.getTopStocks()
        Log.e("Grow App", (networkTasks as Success).data.top_gainers.toString())
        Log.e("Grow App", networkTasks.data.top_losers.toString())
        if (networkTasks is Success) {
            stockLocalDataSource.upsertAllTopGainer(networkTasks.data.top_gainers.toInternalForGainer())
            stockLocalDataSource.upsertAllTopLoser(networkTasks.data.top_losers.toInternalForLoser())
            return networkTasks
        }
        return networkTasks
    }

    suspend fun loadInfraData(ticker:String): NetworkResult<List<StockData>> {
        val networkTasks = remoteDataSource.getStockIntraDay(ticker)
        if (networkTasks is Success) {
            Log.e("Growa",networkTasks.data.toString())
            stockLocalDataSource.upsertAllTimeSeries(networkTasks.data.toInternal())
            Log.e("Gr",networkTasks.data.toString())
            return Success(networkTasks.data)
        }
        Log.e("Growa","app")
        return networkTasks
    }

    suspend fun loadStockData(ticker:String): NetworkResult<StockInfo> {
        val networkTasks = remoteDataSource.getStockInfo(ticker)
        try {
            if (networkTasks is Success) {
                Log.e("Growa", networkTasks.data.toString())
                stockLocalDataSource.insertOrUpdate(networkTasks.data.toInternal())
                Log.e("Gr", networkTasks.data.toString())
                return Success(networkTasks.data)
            }
            Log.e("Growa", "app")
        }catch (e:Exception){
            Log.e("utkarsh",e.message.toString())
        }
        return networkTasks

    }


}