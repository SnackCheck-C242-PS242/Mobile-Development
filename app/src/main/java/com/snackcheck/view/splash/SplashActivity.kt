package com.snackcheck.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.snackcheck.R
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityMainBinding
import com.snackcheck.databinding.ActivitySplashBinding
import com.snackcheck.di.Injection
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.main.MainActivity
import com.snackcheck.view.main.MainViewModel
import com.snackcheck.view.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var viewModel: SplashViewModel
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]

        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.getToken().observe(this) { token ->
            if (token.isNullOrEmpty()) {
                navigateToWelcome()
            } else {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }

    private fun navigateToWelcome() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}