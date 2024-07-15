package com.example.stocksapp.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.stocksapp.data.local.LocalStockDatabase
import com.example.stocksapp.data.local.RemoteDataKeys
import com.example.stocksapp.data.local.RemoteKeysDao
import com.example.stocksapp.data.local.StockCategory
import com.example.stocksapp.data.local.StockDao
import com.example.stocksapp.data.local.StockData
import com.example.stocksapp.data.local.StockTimeSeriesItem
import com.example.stocksapp.data.remote.RemoteDataSource
import com.example.stocksapp.data.remote.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class TopLosersRemoteMediator @Inject constructor(
    private val stockRemoteService: RemoteDataSource,
    private val stockLocalDataSource: StockDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val localStockDatabase: LocalStockDatabase
) : RemoteMediator<Int, StockData>() {

    lateinit var stockList:List<String>

    val stockCategory:String= StockCategory.TOP_LOSERS.stockCategory

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StockData>
    ): MediatorResult {

        Log.e("Mediator",loadType.name)

        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {

            val pageCount=(stockList.size/4+(if(stockList.size%4==0) 0 else 1))

            val stocks= mutableListOf<StockData>()

            val startIdx= (page-1)*4

            val endIdx= if(page==pageCount&&stockList.size%4!=0) (startIdx+stockList.size%4-1) else startIdx+3

            var expectation: Exception?= null

            val handler= CoroutineExceptionHandler{ coroutineContext,throwable->
                expectation =Exception(throwable.message)
            }

            val jobs=CoroutineScope(Dispatchers.IO+handler).launch {
                val jobs = (startIdx..endIdx).map {
                    Log.e("Assignment Network", it.toString() + " Request")
                    CoroutineScope(Dispatchers.IO).launch {
                        val data1 = stockRemoteService.getStockInfo("IBM")
                        val data2 = stockRemoteService.getStockIntraDay("IBM")
                        if ((data1 is Success) && (data2 is Success)) {
                            val stockInfo = data1.data.copy(symbol = stockList[it])
                            val infraData = data2.data
                            val data = StockData(
                                stockCategory = stockCategory,
                                symbol = stockInfo.symbol,
                                assetType = stockInfo.assetType,
                                name = stockInfo.name,
                                description = stockInfo.description,
                                exchange = stockInfo.exchange,
                                sector = stockInfo.sector,
                                industry = stockInfo.industry,
                                marketCapitalization = stockInfo.marketCapitalization,
                                peRatio = stockInfo.peRatio,
                                dividendYield = stockInfo.dividendYield,
                                profitMargin = stockInfo.profitMargin,
                                beta = stockInfo.beta,
                                fiftyTwoWeekHigh = stockInfo.fiftyTwoWeekHigh,
                                fiftyTwoWeekLow = stockInfo.fiftyTwoWeekLow,
                                stocks = infraData.timeSeries.map { stock ->
                                    StockTimeSeriesItem(
                                        open = stock.value.open,
                                        high = stock.value.high,
                                        low = stock.value.low,
                                        volume = stock.value.volume,
                                        close = stock.value.close,
                                        timestamp = stock.key
                                    )
                                }
                            )
                            stocks.add(data)
                        } else if (data1 is Error) {
                            throw Exception(data1.message.toString())
                        } else if (data1 is Exception) {
                            throw IOException(data1.message.toString())
                        } else if (data2 is Error) {
                            throw Exception(data2.message.toString())
                        } else if (data2 is Exception) {
                            throw IOException(data2.message.toString())
                        } else {
                            throw Exception("Nothing")
                        }
                    }
                }

                jobs.forEach {
                    it.join()
                }
            }

            jobs.join()

            if(expectation!=null){
                throw expectation as Exception
            }

            val endOfPaginationReached = page==pageCount

            localStockDatabase.withTransaction {

                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = stocks.map {
                    RemoteDataKeys(
                        stockTicker = it.symbol,
                        prevKey = prevKey,
                        currentPage = page,
                        nextKey = nextKey,
                        stockCategory = StockCategory.TOP_LOSERS.stockCategory
                    )
                }

                remoteKeysDao.insertAll(remoteKeys)
                stockLocalDataSource.insertOrUpdate(stocks.onEachIndexed { _, stock -> stock.page = page })
            }
            Log.e("Groww Assingment Mediator","end $endOfPaginationReached $startIdx $endIdx load ${stockList.size}  ${stocks.size}")
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
        catch (error: Exception) {
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StockData>): RemoteDataKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.symbol?.let { id ->
                remoteKeysDao.getRemoteKeyByAuthorID(stockCategory,id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StockData>): RemoteDataKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { stock ->
            remoteKeysDao.getRemoteKeyByAuthorID(stockCategory,stock.symbol)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StockData>): RemoteDataKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { stock ->
            remoteKeysDao.getRemoteKeyByAuthorID(stockCategory,stock.symbol)
        }
    }

    override suspend fun initialize(): InitializeAction {
        try {
            val cacheTimeout = TimeUnit.MILLISECONDS.convert(100000, TimeUnit.MINUTES)
            val timeStamp = stockLocalDataSource.getOldestTimeStamp(stockCategory)
            Log.e("Assignment", timeStamp.toString())
            return if (System.currentTimeMillis() - timeStamp!! <= cacheTimeout) {
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        } catch (e: Exception) {
            Log.e("Assignment", e.message.toString())
        }
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

}