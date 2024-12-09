package com.snackcheck.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snackcheck.data.UserRepository
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.di.Injection
import com.snackcheck.view.authorization.input_new_password.InputNewPasswordViewModel
import com.snackcheck.view.authorization.login.LoginViewModel
import com.snackcheck.view.authorization.register.SignUpViewModel
import com.snackcheck.view.authorization.reset_password.ResetPasswordViewModel
import com.snackcheck.view.authorization.verification.VerificationViewModel
import com.snackcheck.view.history.all.HistoryViewModel
import com.snackcheck.view.history.detail.HistoryDetailViewModel
import com.snackcheck.view.home.HomeViewModel
import com.snackcheck.view.main.MainViewModel
import com.snackcheck.view.prediction.form.FormViewModel
import com.snackcheck.view.profile.info.ProfileViewModel
import com.snackcheck.view.profile.photo.PhotoProfileViewModel
import com.snackcheck.view.setting.SettingFragmentViewModel
import com.snackcheck.view.splash.SplashViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val pref: UserPreference,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(VerificationViewModel::class.java) -> {
                VerificationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingFragmentViewModel::class.java) -> {
                SettingFragmentViewModel(repository, pref) as T
            }
            modelClass.isAssignableFrom(FormViewModel::class.java) -> {
                FormViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PhotoProfileViewModel::class.java) -> {
                PhotoProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ResetPasswordViewModel::class.java) -> {
                ResetPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(InputNewPasswordViewModel::class.java) -> {
                InputNewPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryDetailViewModel::class.java) -> {
                HistoryDetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: UserPreference): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}