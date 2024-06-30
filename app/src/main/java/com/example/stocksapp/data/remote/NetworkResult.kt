package com.example.stocksapp.data.remote

sealed interface NetworkResult<T : Any>

class Success<T : Any>(val data: T) : NetworkResult<T>
class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>
class Exception<T : Any>(val e: Throwable) : NetworkResult<T>
class Loading<T : Any>() : NetworkResult<T>