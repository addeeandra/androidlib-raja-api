package me.inibukanadit.rajaapi.wilayah

import me.inibukanadit.rajaapi.wilayah.model.Area

class WilayahApiAsyncService private constructor() : WilayahApiService<WilayahApiAsyncWrapper<*>>() {

    companion object {

        private var mInstance: WilayahApiAsyncService? = null

        @Synchronized
        fun instance(): WilayahApiAsyncService {
            if (mInstance == null) {
                mInstance = WilayahApiAsyncService()
            }
            return mInstance as WilayahApiAsyncService
        }

    }

    private val mWilayahApiCoroutineService by lazy { WilayahApiCoroutineService.instance() }

    override fun getKodeUnik() = WilayahApiAsyncWrapper<String> {
        mWilayahApiCoroutineService.getKodeUnik()
    }

    override fun getProvinsi(kodeUnik: String) = WilayahApiAsyncWrapper<List<Area>> {
        mWilayahApiCoroutineService.getProvinsi(kodeUnik)
    }

    override fun getKabupaten(kodeUnik: String, provinsiId: Int) = WilayahApiAsyncWrapper<List<Area>> {
        mWilayahApiCoroutineService.getKabupaten(kodeUnik, provinsiId)
    }

    override fun getKecamatan(kodeUnik: String, kabupatenId: Int) = WilayahApiAsyncWrapper<List<Area>> {
        mWilayahApiCoroutineService.getKecamatan(kodeUnik, kabupatenId)
    }

    override fun getKelurahan(kodeUnik: String, kecamatanId: Int) = WilayahApiAsyncWrapper<List<Area>> {
        mWilayahApiCoroutineService.getKelurahan(kodeUnik, kecamatanId)
    }

}