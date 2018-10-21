package me.inibukanadit.rajaapi.wilayah

import me.inibukanadit.rajaapi.wilayah.util.sendGetRequest
import org.json.JSONObject

class WilayahApiCoroutineService : WilayahApi<Result>() {

    override suspend fun getKodeUnik(): Result {
        val response = sendGetRequest("$API_HOST/poe").await()
        val json = JSONObject(response)

        return if (getStatusSuccess(json)) {
            Result.Success(json.getString("token"))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    override suspend fun getProvinsi(kodeUnik: String): Result {
        return getArea(kodeUnik, PATH_PROVINCE)
    }

    override suspend fun getKabupaten(kodeUnik: String, provinsiId: Int): Result {
        return getArea(kodeUnik, PATH_CITY, "idpropinsi", provinsiId)
    }

    override suspend fun getKecamatan(kodeUnik: String, kabupatenId: Int): Result {
        return getArea(kodeUnik, PATH_DISTRICT, "idkabupaten", kabupatenId)
    }

    override suspend fun getKelurahan(kodeUnik: String, kecamatanId: Int): Result {
        return getArea(kodeUnik, PATH_VILLAGE, "idkecamatan", kecamatanId)
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
            Result.Success(getListAreaFromJson(json))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }
}