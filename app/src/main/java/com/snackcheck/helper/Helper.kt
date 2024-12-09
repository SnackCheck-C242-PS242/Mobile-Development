package com.snackcheck.helper

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String.withDateFormat(): String {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        originalFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = originalFormat.parse(this)
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        formatter.format(date!!)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}
