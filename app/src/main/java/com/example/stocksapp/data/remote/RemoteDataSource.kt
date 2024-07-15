package com.example.stocksapp.data.remote

import android.util.Log
import com.example.stocksapp.data.dto.StockInfo
import com.example.stocksapp.data.dto.TimeSeriesData
import com.example.stocksapp.data.dto.TopStockData
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

@Singleton
class RemoteDataSource @Inject constructor(
    private val remoteService: RemoteService
) {

    suspend fun getTopStocks(): NetworkResult<TopStockData> =
        handleDataResult(extra = { it }) {
            Log.e("Network Request Groww Assignment", "Top Stock Requested")
            val result = remoteService.getTopStocks()
            Log.e("Network Request Groww Assignment", "Top Stock Request Completed")
            result
        }


    suspend fun getStockInfo(ticker:String): NetworkResult<StockInfo> =
        handleDataResult(extra = {it}) {
            Log.e("Network Request Groww Assignment","Stock Info for $ticker Requested")
            val result= remoteService.getTickerInfo(ticker = ticker)
            Log.e("Network Request Groww Assignment","Stock Info for $ticker Request Completed")
            result
        }

    suspend fun getStockIntraDay(ticker: String): NetworkResult<TimeSeriesData> =
        handleDataResult(extra = { it }) {
            Log.e("Network Request Groww Assignment", "Stock Infra Data for $ticker Requested")
            val result = remoteService.getIntraDayData(ticker = ticker)
            Log.e(
                "Network Request Groww Assignment",
                "Stock Infra Data for $ticker Request Completed"
            )
            result
        }

    private suspend fun <T : Any,B:Any > handleDataResult(
        extra: ((T) -> B),
        execute: suspend () -> Response<T>
    ): NetworkResult<B> {
        return try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                    Success(extra(body))
            } else {
                Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            Log.e("Network Error trace",e.stackTrace.toString())
            Error(code = e.code(), message = e.message())
        }
        catch (e: Exception) {
            Log.e("Network Error Trace",e.stackTraceToString())
            Log.e("Network Error",e.message.toString())
            Error(code = 500, message = e.message.toString())
        }
        catch (e: Throwable) {
            Log.e("Network Error trace",e.stackTraceToString())
            Log.e("Network Error",e.message.toString())
            com.example.stocksapp.data.remote.Exception(e)
        }
    }
}