package com.example.stocksapp.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.dto.StockInfo
import com.example.stocksapp.data.dto.TimeSeriesData
import com.example.stocksapp.data.dto.TopStockData
import com.example.stocksapp.data.remote.Properties.KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    @GET("/query?function=TOP_GAINERS_LOSERS&apikey=${KEY}")
    suspend fun getTopStocks(): Response<TopStockData>

    @GET("/query")
    suspend fun getTickerInfo(
        @Query("function") function: String="OVERVIEW",
        @Query("symbol") ticker: String,
        @Query("apikey") apiKey: String= KEY,
    ): Response<StockInfo>

    @GET("/query")
    suspend fun getIntraDayData(
        @Query("function") function: String="TIME_SERIES_INTRADAY",
        @Query("symbol") ticker: String,
        @Query("interval") interval: String="5min",
        @Query("outputsize") outputSize: String="full",
        @Query("apikey") apiKey: String= KEY
    ): Response<TimeSeriesData>

}

fun hasNetwork(context: Context): Boolean {
    val connectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}

