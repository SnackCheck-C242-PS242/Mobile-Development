package com.snackcheck.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.snackcheck.data.remote.model.ProfileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_setting")
    private val token = stringPreferencesKey("token")

    private val nameKey = stringPreferencesKey("name")
    private val emailKey = stringPreferencesKey("email")
    private val username = stringPreferencesKey("username")
    private val profilePhotoUrlKey = stringPreferencesKey("profile_picture")


    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[token]
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[this.token] = token
        }
    }

    fun getUsername(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[username]
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[this.username] = username
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveProfile(profileData: ProfileData) {
        dataStore.edit { preferences ->
            preferences[nameKey] = profileData.fullName
            preferences[username] = profileData.username
            preferences[emailKey] = profileData.email
            preferences[profilePhotoUrlKey] = profileData.profilePhoto
        }
    }

    fun getProfileData(): Flow<ProfileData?> {
        return dataStore.data.map { preferences ->
            val name = preferences[nameKey] ?: ""
            val username = preferences[username] ?: ""
            val email = preferences[emailKey] ?: ""
            val profilePhotoUrl = preferences[profilePhotoUrlKey] ?: ""

            ProfileData(name, username, email, profilePhotoUrl)
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}