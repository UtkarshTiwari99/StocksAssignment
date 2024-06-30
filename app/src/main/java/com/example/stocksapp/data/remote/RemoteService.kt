package com.example.stocksapp.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.dto.StockInfo
import com.example.stocksapp.data.dto.TimeSeriesData
import com.example.stocksapp.data.dto.TopStockData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    @GET("/query?function=TOP_GAINERS_LOSERS&apikey=demo")
    suspend fun getTopStocks(): Response<TopStockData>

    @GET("/query?function=OVERVIEW&symbol=IBM&apikey=demo")
    suspend fun getTickerInfo(
//        @Query("ticker") ticker: String
    ): Response<StockInfo>

    @GET("/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&outputsize=full&apikey=demo")
    suspend fun getIntraDayData(
//        @Query("ticker") ticker: String
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

