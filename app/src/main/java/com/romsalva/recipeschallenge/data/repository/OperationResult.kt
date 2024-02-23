package com.romsalva.recipeschallenge.data.repository

sealed class OperationResult {
    data object Success : OperationResult()
    data class Error(val throwable: Throwable) : OperationResult() {
        val message get() = throwable.message.orEmpty()
    }
}
