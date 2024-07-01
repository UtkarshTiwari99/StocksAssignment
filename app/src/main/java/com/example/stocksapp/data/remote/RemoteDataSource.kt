package com.example.stocksapp.data.remote

import android.util.Log
import com.example.stocksapp.data.dto.StockInfo
import com.example.stocksapp.data.dto.TopStockData
import com.example.stocksapp.data.dto.toExternal
import com.example.stocksapp.data.dto.toInternal
import com.example.stocksapp.data.local.StockInfoEntity
import com.example.stocksapp.data.model.StockData
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val remoteService: RemoteService
) {
    suspend fun getTopStocks(): NetworkResult<TopStockData> =
        handleDataResult(extra = { it }) { remoteService.getTopStocks() }

    suspend fun getStockInfo(ticker:String): NetworkResult<StockInfo> =
        handleDataResult(extra = {it}) { remoteService.getTickerInfo(ticker = ticker) }

    suspend fun getStockIntraDay(ticker:String): NetworkResult<List<StockData>> =
        handleDataResult(extra = {it.toExternal()}) { remoteService.getIntraDayData(ticker=ticker) }

    private suspend fun <T : Any,B:Any > handleDataResult(
        extra: ((T) -> B),
        execute: suspend () -> Response<T>
    ): NetworkResult<B> {
        return try {
            val response = execute()
            val body = response.body()
            Log.e("uth",response.toString())
            Log.e("uth",response.body().toString())
            if (response.isSuccessful && body != null) {
                    Success(extra(body))
            } else {
                Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            Log.e("uth error",e.stackTrace.toString())
            Error(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            Log.e("uth error trace",e.stackTraceToString())
            Log.e("uth error",e.message.toString())
            Exception(e)
        }
    }
}