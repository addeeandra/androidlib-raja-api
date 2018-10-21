package me.inibukanadit.rajaapi.wilayah

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import me.inibukanadit.rajaapi.wilayah.util.sendGetRequest
import org.json.JSONObject

class WilayahApiCoroutineService : WilayahApi<Deferred<Result>>() {

    companion object {

        private var mInstance: WilayahApiCoroutineService? = null

        @Synchronized
        fun getInstance(): WilayahApiCoroutineService {
            if (mInstance == null) {
                mInstance = WilayahApiCoroutineService()
            }
            return mInstance as WilayahApiCoroutineService
        }

    }

    override fun getKodeUnik(): Deferred<Result> = GlobalScope.async {
        val response = sendGetRequest("$API_HOST/poe").await()
        val json = JSONObject(response)

        if (getStatusSuccess(json)) {
            Result.Success(json.getString("token"))
        } else {
            Result.Error(getStatusCode(json), getStatusMessage(json))
        }
    }

    override fun getProvinsi(kodeUnik: String): Deferred<Result> = GlobalScope.async {
        getArea(kodeUnik, PATH_PROVINCE)
    }

    override fun getKabupaten(kodeUnik: String, provinsiId: Int): Deferred<Result> = GlobalScope.async {
        getArea(kodeUnik, PATH_CITY, "idpropinsi", provinsiId)
    }

    override fun getKecamatan(kodeUnik: String, kabupatenId: Int): Deferred<Result> = GlobalScope.async {
        getArea(kodeUnik, PATH_DISTRICT, "idkabupaten", kabupatenId)
    }

    override fun getKelurahan(kodeUnik: String, kecamatanId: Int): Deferred<Result> = GlobalScope.async {
        getArea(kodeUnik, PATH_VILLAGE, "idkecamatan", kecamatanId)
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