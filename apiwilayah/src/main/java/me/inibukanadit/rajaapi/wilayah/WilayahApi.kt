package me.inibukanadit.rajaapi.wilayah

import me.inibukanadit.rajaapi.wilayah.model.Area
import org.json.JSONObject

abstract class WilayahApi<T> {

    companion object {
        const val API_HOST = BuildConfig.API_HOST
        const val API_WILAYAH_URL = API_HOST + "MeP7c5ne%s/m/wilayah%s"

        const val PATH_PROVINCE = "provinsi"
        const val PATH_CITY = "kabupaten"
        const val PATH_DISTRICT = "kecamatan"
        const val PATH_VILLAGE = "kelurahan"
    }

    abstract suspend fun getKodeUnik(): T
    abstract suspend fun getProvinsi(kodeUnik: String): T
    abstract suspend fun getKabupaten(kodeUnik: String, provinsiId: Int): T
    abstract suspend fun getKecamatan(kodeUnik: String, kabupatenId: Int): T
    abstract suspend fun getKelurahan(kodeUnik: String, kecamatanId: Int): T

    protected fun buildUrl(uniqueCode: String, path: String): String {
        return String.format(API_WILAYAH_URL, uniqueCode, path)
    }

    protected fun getListAreaFromJson(obj: JSONObject): List<Area> {
        val data = obj.getJSONArray("data")
        val modelList = mutableListOf<Area>()

        for (i in (0 until data.length())) {
            val id = data.getJSONObject(i).getInt("id")
            val name = data.getJSONObject(i).getString("name")

            modelList += Area(id, name)
        }

        return modelList
    }

    protected fun getStatusSuccess(obj: JSONObject): Boolean {
        return if (obj.has("success")) {
            obj.getBoolean("success")
        } else {
            false
        }
    }

    protected fun getStatusCode(obj: JSONObject): Int {
        return if (obj.has("code")) {
            obj.getInt("code")
        } else {
            500
        }
    }

    protected fun getStatusMessage(obj: JSONObject): String {
        return if (obj.has("data")) {
            obj.getString("data")
        } else {
            ""
        }
    }

}