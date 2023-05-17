package com.moviemanager.ui

sealed class ViewState<T> {
    data class Loading<T>(
        val isLoading: Boolean = true
    ) : ViewState<T>()

    data class Success<T>(
        val data: T
    ) : ViewState<T>()

    data class Error<T>(
        val message: String
    ) : ViewState<T>()
}
