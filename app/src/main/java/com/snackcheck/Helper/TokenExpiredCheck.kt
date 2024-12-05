package com.snackcheck.Helper

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
fun isTokenExpired(refreshToken: String): Boolean {
    // Decode JWT token (decode bagian payload)
    val parts = refreshToken.split(".")
    if (parts.size != 3) {
        return true // Invalid JWT format
    }

    val payload = parts[1]
    val decodedPayload = String(Base64.getDecoder().decode(payload))
    val jsonPayload = JSONObject(decodedPayload)

    // Ambil waktu kedaluwarsa (exp) dari payload
    val expTime = jsonPayload.optLong("exp", 0)
    val currentTime = System.currentTimeMillis() / 1000 // waktu saat ini dalam detik

    return expTime <= currentTime // Jika waktu kadaluarsa lebih kecil dari waktu saat ini, token sudah kadaluarsa
}