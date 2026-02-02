package fr.leboncoin.domain

sealed class AlbumResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : AlbumResult<T>(data)
    class Loading<T>(data: T? = null) : AlbumResult<T>(data)
    class Error<T>(message: String, data: T? = null) : AlbumResult<T>(data, message)
}