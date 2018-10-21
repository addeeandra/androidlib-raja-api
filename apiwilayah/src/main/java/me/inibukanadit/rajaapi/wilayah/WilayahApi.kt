package me.inibukanadit.rajaapi.wilayah

import me.inibukanadit.rajaapi.wilayah.model.Area

object WilayahApi {

    private fun <T> safeResultDataCast(result: Result): T? {
        return try {
            when (result) {
                is Result.Success<*> -> result.data as T
                is Result.Error -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getUniqueCode(result: Result): String? {
        return safeResultDataCast(result)
    }

    fun getAreaList(result: Result): List<Area>? {
        return safeResultDataCast(result)
    }

    fun getDataMessage(result: Result): String {
        return safeResultDataCast(result) ?: "No message"
    }

}