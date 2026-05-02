package com.streamtv.app.data.prefs

import android.content.Context
import android.content.SharedPreferences

class AppPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("streamtv_prefs", Context.MODE_PRIVATE)

    var serverIp: String
        get() = prefs.getString(KEY_SERVER_IP, "") ?: ""
        set(value) = prefs.edit().putString(KEY_SERVER_IP, value).apply()

    var apiKey: String
        get() = prefs.getString(KEY_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_API_KEY, value).apply()

    fun getBaseUrl(): String = "http://$serverIp"

    fun isConfigured(): Boolean = serverIp.isNotEmpty()

    fun clear() = prefs.edit().clear().apply()

    companion object {
        private const val KEY_SERVER_IP = "server_ip"
        private const val KEY_API_KEY = "api_key"
    }
}
