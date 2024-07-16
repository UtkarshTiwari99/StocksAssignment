package com.example.stocksapp.data.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.dto.StockInfo
import com.example.stocksapp.data.dto.TimeSeriesData
import com.example.stocksapp.data.local.StockInfoEntity
import com.example.stocksapp.data.model.StockData
import com.example.stocksapp.data.remote.Error
import com.example.stocksapp.data.remote.Exception
import com.example.stocksapp.data.remote.Loading
import com.example.stocksapp.data.remote.Success
import com.example.stocksapp.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.sql.Time
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class StockViewModel @Inject constructor(private val stockRepository: StockRepository) :
    ViewModel() {

    private var _topGainers: MutableStateFlow<List<Stock>> = MutableStateFlow(emptyList())
    val topGainers: Flow<List<Stock>> = _topGainers

    private var _stockInfo: MutableStateFlow<StockInfo?> = MutableStateFlow(null)
    val stockInfo: Flow<StockInfo?> = _stockInfo

    private var _topLosers: MutableStateFlow<List<Stock>> = MutableStateFlow(emptyList())
    val topLosers: Flow<List<Stock>> = _topLosers

    private var _infraData: MutableStateFlow<List<StockData>> = MutableStateFlow(emptyList())
    val infraData: Flow<List<StockData>> = _infraData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading

    private val _isGraphLoading = MutableStateFlow(false)
    val isGraphLoading: Flow<Boolean> = _isGraphLoading

    private val _isError = MutableStateFlow<String?>(null)
    val isError: Flow<String?> = _isError

    fun loadTopStocks() {
        _isLoading.update {
            true
        }
        viewModelScope.launch {
            when (val data = stockRepository.load()) {
                is Success -> {
                    launch {
                        stockRepository.observeAllTopGainer().collect {
                            _topGainers.value = it
                            Log.e("Grow", it.toString())
                        }
                    }
                    launch {
                        stockRepository.observeAllTopLoser().collect {
                            _topLosers.value = it
                            Log.e("Grow", it.toString())
                        }
                    }
                }


                is Loading -> {
                    _isLoading.update {
                        true
                    }
                }

                is Error -> {
                    _isError.update {
                        data.message
                    }
                }

                is Exception -> {
                    _isError.update {
                        data.e.message
                    }
                }
            }
            _isLoading.update {
                false
            }
        }
    }

    fun loadInfraData(ticker: String) {
        _isGraphLoading.update {
            true
        }
        viewModelScope.launch {
            when (val data = stockRepository.loadInfraData(ticker)) {
                is Success -> {
                    launch {
                        stockRepository.observeInfraDayData(ticker).collect {
                            _infraData.value = it
                            Log.e("Grow", it.toString())
                            _isGraphLoading.update {
                                false
                            }
                        }
                    }
                }

                is Loading -> {
                    _isGraphLoading.update {
                        true
                    }
                }

                is Error -> {
                    _isError.update {
                        data.message
                    }
                }

                is Exception -> {
                    _isError.update {
                        data.e.message
                    }
                }
            }
            _isGraphLoading.update {
                false
            }
        }
    }

    fun loadStockInfo(stock: Stock) {
        Log.e("uth","load StockINfo")
        _isLoading.update {
            true
        }
        viewModelScope.launch {
            Log.e("uth","load StockINfo launcehed")
            try {
                val data = stockRepository.loadStockData(stock.ticker)
                when (data) {
                    is Success -> {
                        launch {
                            stockRepository.observeStockInfo(ticker = stock.ticker).collect {
                                if (it != null) {
                                    _stockInfo.value = it.copy(
                                        change_percentage = stock.change_percentage,
                                        price = stock.price.toString(),
                                        change_amount = stock.change_amount.toString()
                                    )
                                }
                                Log.e("Grow", it.toString())
                            }
                        }
                    }

                    is Loading -> {
                        _isLoading.update {
                            true
                        }
                    }

                    is Error -> {
                        _isError.update {
                            data.message
                        }
                    }

                    is Exception -> {
                        _isError.update {
                            data.e.message
                        }
                    }
                }
                _isLoading.update {
                    false
                }
            }catch (e:kotlin.Exception){
                Log.e("uth",e.message.toString())
            }
        }
    }

}