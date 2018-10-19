package me.inibukanadit.rajaapi.wilayah

sealed class Result {

    data class Success<T>(val data: T): Result()
    data class Error(val code: Int, val message: String): Result()

}