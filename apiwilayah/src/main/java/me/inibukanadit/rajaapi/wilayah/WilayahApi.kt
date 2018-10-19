package me.inibukanadit.rajaapi.wilayah

import me.inibukanadit.rajaapi.wilayah.model.Area
import me.inibukanadit.rajaapi.wilayah.model.Kabupaten
import me.inibukanadit.rajaapi.wilayah.model.Kecamatan
import me.inibukanadit.rajaapi.wilayah.model.Provinsi
import me.inibukanadit.rajaapi.wilayah.util.sendGetRequest
import org.json.JSONObject

object WilayahApi {

    private const val API_HOST = BuildConfig.API_HOST
    private const val API_WILAYAH_URL = API_HOST + "MeP7c5ne%s/m/wilayah%s"

    private fun buildUrl(uniqueCode: String, path: String): String {
        return String.format(API_WILAYAH_URL, uniqueCode, path)
    }

    suspend fun getKodeUnik(): Result {
        val response = sendGetRequest("$API_HOST/poe").await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(json.getString("token"))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    suspend fun getProvinsi(kodeUnik: String): Result {
        val response = sendGetRequest(buildUrl(kodeUnik, "/provinsi")).await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(responseToModel<Provinsi>(json))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    suspend fun getKabupaten(kodeUnik: String, provinsiId: String): Result {
        val response = sendGetRequest(buildUrl(kodeUnik, "/kabupaten?idpropinsi=$provinsiId")).await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(responseToModel<Kabupaten>(json))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    suspend fun getKecamatan(kodeUnik: String, kabupatenId: String): Result {
        val response = sendGetRequest(buildUrl(kodeUnik, "/kecamatan?idkabupaten=$kabupatenId")).await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(responseToModel<Kecamatan>(json))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    suspend fun getKelurahan(kodeUnik: String, kecamatanId: String): Result {
        val response = sendGetRequest(buildUrl(kodeUnik, "/kelurahan?idkecamatan=$kecamatanId")).await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(responseToModel<Kecamatan>(json))
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