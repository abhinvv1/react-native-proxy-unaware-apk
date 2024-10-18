package com.proxyunawarenew 

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import kotlin.concurrent.thread

class ProxyUnawareModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String = "ProxyUnawareModule"

    @ReactMethod
    fun sendRequest(urlString: String, promise: Promise) {
        thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection(Proxy.NO_PROXY) as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                // Disable proxy usage
                System.setProperty("http.proxyHost", "")
                System.setProperty("http.proxyPort", "")
                System.setProperty("https.proxyHost", "")
                System.setProperty("https.proxyPort", "")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var inputLine: String?
                    while (reader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    reader.close()
                    promise.resolve(response.toString())
                } else {
                    promise.reject("HTTP_ERROR", "HTTP Error: $responseCode")
                }
            } catch (e: Exception) {
                promise.reject("ERROR", "Error: ${e.message}")
            }
        }
    }
}
