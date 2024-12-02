package com.snackcheck.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snackcheck.R
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.main.MainActivity
import com.snackcheck.view.main.MainViewModel
import com.snackcheck.view.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {

    private val pref = UserPreference.getInstance(dataStore)
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    private val viewModel by viewModels<SplashViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navigateToWelcome()
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