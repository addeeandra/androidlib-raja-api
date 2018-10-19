package me.inibukanadit.rajaapi.wilayah.util

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import java.net.URL


fun sendGetRequest(url: String): Deferred<String> {
    return GlobalScope.async { URL(url).readText() }
}