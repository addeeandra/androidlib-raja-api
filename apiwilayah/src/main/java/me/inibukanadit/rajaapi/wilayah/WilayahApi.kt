package me.inibukanadit.rajaapi.wilayah

import me.inibukanadit.rajaapi.wilayah.model.Area
import me.inibukanadit.rajaapi.wilayah.util.sendGetRequest
import org.json.JSONObject

object WilayahApi {

    internal fun <T> safeResultDataCast(result: Result): T? {
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

    /**
     * LINES BELOW GOING TO DEPRECATED
     * TODO remove these lines someday
     */

    private const val API_HOST = BuildConfig.API_HOST
    private const val API_WILAYAH_URL = API_HOST + "MeP7c5ne%s/m/wilayah%s"

    private fun buildUrl(uniqueCode: String, path: String): String {
        return String.format(API_WILAYAH_URL, uniqueCode, path)
    }

    @Deprecated("This method will be removed soon", ReplaceWith("WilayahApiCoroutineService", "me.inibukanadit.rajaapi.wilayah.WilayahApiCoroutineService"))
    suspend fun getKodeUnik(): Result {
        val response = sendGetRequest("$API_HOST/poe").await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(json.getString("token"))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    @Deprecated("This method will be removed soon", ReplaceWith("WilayahApiCoroutineService", "me.inibukanadit.rajaapi.wilayah.WilayahApiCoroutineService"))
    suspend fun getProvinsi(kodeUnik: String): Result {
        return getArea(kodeUnik, "provinsi")
    }

    @Deprecated("This method will be removed soon", ReplaceWith("WilayahApiCoroutineService", "me.inibukanadit.rajaapi.wilayah.WilayahApiCoroutineService"))
    suspend fun getKabupaten(kodeUnik: String, provinsiId: Int): Result {
        return getArea(kodeUnik, "kabupaten", "idpropinsi", provinsiId)
    }

    @Deprecated("This method will be removed soon", ReplaceWith("WilayahApiCoroutineService", "me.inibukanadit.rajaapi.wilayah.WilayahApiCoroutineService"))
    suspend fun getKecamatan(kodeUnik: String, kabupatenId: Int): Result {
        return getArea(kodeUnik, "kecamatan", "idkabupaten", kabupatenId)
    }

    @Deprecated("This method will be removed soon", ReplaceWith("WilayahApiCoroutineService", "me.inibukanadit.rajaapi.wilayah.WilayahApiCoroutineService"))
    suspend fun getKelurahan(kodeUnik: String, kecamatanId: Int): Result {
        return getArea(kodeUnik, "kelurahan", "idkecamatan", kecamatanId)
    }

    private suspend fun getArea(
            kodeUnik: String,
            areaName: String,
            keywordId: String? = null,
            id: Int? = null
    ): Result {
        val rawUrl = "/$areaName" + if (keywordId != null) "?$keywordId=$id" else ""
        val url = buildUrl(kodeUnik, rawUrl)

        val response = sendGetRequest(url).await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(responseToModel<Area>(json))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    private fun <T : Area> responseToModel(obj: JSONObject): List<T> {
        val data = obj.getJSONArray("data")
        val modelList = mutableListOf<T>()

        for (i in (0 until data.length())) {
            val id = data.getJSONObject(i).getInt("id")
            val name = data.getJSONObject(i).getString("name")

            modelList += Area(id, name) as T
        }

        return modelList
    }

    private fun getStatusSuccess(obj: JSONObject): Boolean {
        return if (obj.has("success")) {
            obj.getBoolean("success")
        } else {
            false
        }
    }

    private fun getStatusCode(obj: JSONObject): Int {
        return if (obj.has("code")) {
            obj.getInt("code")
        } else {
            500
        }
    }

    private fun getStatusMessage(obj: JSONObject): String {
        return if (obj.has("data")) {
            obj.getString("data")
        } else {
            ""
        }
    }

}