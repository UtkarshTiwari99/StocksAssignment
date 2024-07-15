package com.example.stocksapp.data.repository

import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.dto.TopStockData
import com.example.stocksapp.data.local.StockDao
import com.example.stocksapp.data.remote.NetworkResult
import com.example.stocksapp.data.remote.RemoteDataSource
import com.example.stocksapp.data.remote.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.stocksapp.data.local.StockCategory
import com.example.stocksapp.data.local.StockData
import com.example.stocksapp.data.local.TopStock
import kotlinx.coroutines.flow.emptyFlow

@Singleton
class StockRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val stockLocalDataSource: StockDao,
    private val topGainersRemoteMediator: TopGainersRemoteMediator,
    private val topLosersRemoteMediator: TopLosersRemoteMediator
) {

    fun observeTopStocks() : Flow<List<TopStock>> {
        return stockLocalDataSource.getTopStocks().map {
            if(it==null||it.isEmpty())
                emptyList<TopStock>()
            it
        }
    }

    suspend fun load(): NetworkResult<TopStockData> {
        try {
            val networkTasks = remoteDataSource.getTopStocks()
            if (networkTasks is Success) {
                Log.e("Grow App", networkTasks.data.top_gainers.toString())
                Log.e("Grow App", networkTasks.data.top_losers.toString())
                val topGainers = networkTasks.data.top_gainers
                val topLosers = networkTasks.data.top_losers
                stockLocalDataSource.upsertAllTopStocks(
                    listOf(
                        TopStock(
                            StockCategory.TOP_GAINERS.stockCategory,
                            stocks = topGainers.map {
                                Stock(
                                    it.ticker,
                                    it.price,
                                    change_amount = it.change_amount,
                                    change_percentage = it.change_percentage,
                                    it.volume
                                )
                            }), TopStock(StockCategory.TOP_LOSERS.stockCategory, topLosers.map {
                            Stock(
                                it.ticker,
                                it.price,
                                change_amount = it.change_amount,
                                change_percentage = it.change_percentage,
                                it.volume
                            )
                        })
                    )
                )
                return networkTasks
            }
            return networkTasks
        } catch (e: Exception) {
            Log.e("Repository Network Exception", e.message.toString())
            return com.example.stocksapp.data.remote.Exception(e.cause ?: e.initCause(e.cause))
        }
    }

    var stockDataForTopGainers:  Flow<PagingData<StockData>> = emptyFlow()

    var stockDataForTopLosers:  Flow<PagingData<StockData>> = emptyFlow()

    @OptIn(ExperimentalPagingApi::class)
    fun initStockInfo(listOfGainers:List<String>,listOfLosers: List<String>) {
        topGainersRemoteMediator.stockList= listOfGainers
        topLosersRemoteMediator.stockList= listOfLosers
        stockDataForTopGainers = Pager(
            config = PagingConfig(
                pageSize = 4,
                prefetchDistance = 4,
                initialLoadSize = 4,
            ),
            pagingSourceFactory = {
                stockLocalDataSource.getStockInfoPage(topGainersRemoteMediator.stockCategory)
            }, remoteMediator = topGainersRemoteMediator
        ).flow
        stockDataForTopLosers = Pager(
            config = PagingConfig(
                pageSize = 4,
                prefetchDistance = 4,
                initialLoadSize = 4,
            ),
            pagingSourceFactory = {
                stockLocalDataSource.getStockInfoPage(topLosersRemoteMediator.stockCategory)
            }, remoteMediator = topLosersRemoteMediator
        ).flow
    }

}