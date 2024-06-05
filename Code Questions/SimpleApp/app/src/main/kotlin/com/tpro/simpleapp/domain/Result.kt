package com.tpro.simpleapp.domain

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(
        val exception: Exception? = null,
        val errorMessage: String? = null
    ) :
        Result<Nothing>()
}