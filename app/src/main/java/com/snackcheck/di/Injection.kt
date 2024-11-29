package com.snackcheck.di

import android.content.Context
import com.snackcheck.data.UserRepository
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}