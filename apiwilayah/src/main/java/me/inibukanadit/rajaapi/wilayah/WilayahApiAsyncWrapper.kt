package me.inibukanadit.rajaapi.wilayah

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

class WilayahApiAsyncWrapper<T>(private val mPendingResult: () -> Deferred<Result>) {

    fun execute(callback: Callback<T>) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = mPendingResult().await()
            val data = WilayahApi.safeResultDataCast<T>(result)

            if (data != null) callback.onResult(data, null)
            else callback.onResult(null, WilayahApi.getDataMessage(result))
        }
    }

    interface Callback<T> {
        fun onResult(data: T?, error: String?)
    }

}