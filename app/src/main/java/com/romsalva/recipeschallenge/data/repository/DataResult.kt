package com.romsalva.recipeschallenge.data.repository

sealed class DataResult<T> {
    class Success<T>(val value: T) : DataResult<T>()
    class Empty<T> : DataResult<T>()
    sealed class Error<T>(val throwable: Throwable) : DataResult<T>() {
        val message: String = throwable.message.orEmpty()

        class Network<T>(throwable: Throwable) : Error<T>(throwable)
        class Database<T>(throwable: Throwable) : Error<T>(throwable)
        class Other<T>(throwable: Throwable) : Error<T>(throwable)
    }
}
