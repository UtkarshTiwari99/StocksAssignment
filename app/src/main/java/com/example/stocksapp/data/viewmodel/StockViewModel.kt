package com.example.stocksapp.data.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.dto.StockInfo
import com.example.stocksapp.data.local.StockCategory
import com.example.stocksapp.data.local.StockData
import com.example.stocksapp.data.local.TopStock
import com.example.stocksapp.data.remote.Error
import com.example.stocksapp.data.remote.Exception
import com.example.stocksapp.data.remote.Loading
import com.example.stocksapp.data.remote.Success
import com.example.stocksapp.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val stockRepository: StockRepository) :
    ViewModel() {

    var stockForTopGainers: Flow<PagingData<StockData>> = emptyFlow()

    var stockForTopLosers: Flow<PagingData<StockData>> = emptyFlow()

    private var _topStocks: MutableStateFlow<List<TopStock>> =
        MutableStateFlow(emptyList())
    val topStocks: Flow<List<TopStock>> = _topStocks

    private var _currentStock= MutableStateFlow(StockData())
    val currentStock: Flow<StockData> = _currentStock

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading

    private val _isError = MutableStateFlow<String?>(null)
    val isError: Flow<String?> = _isError

    private val _isGraphLoading = MutableStateFlow(false)
    val isGraphLoading: Flow<Boolean> = _isGraphLoading

    init {
        viewModelScope.launch {
            stockRepository.observeTopStocks().collectLatest { value ->
                Log.e("ViewModel Groww Assignment", value.toString())
                _topStocks.value = value
                stockRepository.initStockInfo(
                    value.firstOrNull { it.stockCategory == StockCategory.TOP_GAINERS.stockCategory }?.stocks?.map { it.ticker }?: emptyList(),
                    value.firstOrNull { it.stockCategory == StockCategory.TOP_LOSERS.stockCategory }?.stocks?.map { it.ticker }?: emptyList()
                )
                stockForTopGainers = stockRepository.stockDataForTopGainers.cachedIn(viewModelScope)
                stockForTopLosers = stockRepository.stockDataForTopLosers.cachedIn(viewModelScope)
            }
        }
        loadTopStock()
    }

    fun loadStockDetail(stockData:StockData,stock:Stock){
        _currentStock.update {
                stockData.change_amount = stock.change_amount
                stockData.change_percentage = stock.change_percentage
                stockData.price = stock.price
            stockData
        }
    }

    fun loadTopStock() {
        _isLoading.update {
            true
        }
        viewModelScope.launch {
            when (val data = stockRepository.load()) {
                is Success -> {
                    _isLoading.update {
                        false
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

    fun startGraphLoad() {
        viewModelScope.launch {
            _isGraphLoading.update {
                true
            }
            delay(10000)
            _isGraphLoading.update {
                false
            }
        }
    }

}