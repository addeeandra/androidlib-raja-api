package me.inibukanadit.rajaapi.wilayah

import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

class WilayahApiService(private val mResultCallback: ResultCallback) : WilayahApi<Unit>() {

    companion object {

        private var mInstance: WilayahApiService? = null

        @Synchronized
        fun instance(callback: ResultCallback): WilayahApiService {
            if (mInstance == null) {
                mInstance = WilayahApiService(callback)
            }
            return mInstance as WilayahApiService
        }

    }

    private val mWilayahApiCoroutineService by lazy { WilayahApiCoroutineService.instance() }

    override fun getKodeUnik() {
        GlobalScope.launch {
            val result = mWilayahApiCoroutineService.getKodeUnik().await()
            mResultCallback.onResult(result)
        }
    }

    override fun getProvinsi(kodeUnik: String) {
        GlobalScope.launch {
            val result = mWilayahApiCoroutineService.getProvinsi(kodeUnik).await()
            mResultCallback.onResult(result)
        }
    }

    override fun getKabupaten(kodeUnik: String, provinsiId: Int) {
        GlobalScope.launch {
            val result = mWilayahApiCoroutineService.getKabupaten(kodeUnik, provinsiId).await()
            mResultCallback.onResult(result)
        }
    }

    override fun getKecamatan(kodeUnik: String, kabupatenId: Int) {
        GlobalScope.launch {
            val result = mWilayahApiCoroutineService.getKecamatan(kodeUnik, kabupatenId).await()
            mResultCallback.onResult(result)
        }
    }

    override fun getKelurahan(kodeUnik: String, kecamatanId: Int) {
        GlobalScope.launch {
            val result = mWilayahApiCoroutineService.getKelurahan(kodeUnik, kecamatanId).await()
            mResultCallback.onResult(result)
        }
    }

    interface ResultCallback {

        fun onResult(result: Result)

    }

}