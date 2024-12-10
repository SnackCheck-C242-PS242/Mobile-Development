package com.snackcheck.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snackcheck.R
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityMainBinding
import com.snackcheck.di.Injection
import com.snackcheck.helper.isTokenExpired
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.getToken().observe(this) { token ->
            if (token.isNullOrEmpty()) {
                viewModel.clearUserData()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    isTokenExpired(token)
                } else {
                    true
                }
            ) {
                viewModel.clearUserData()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModel.getProfile()

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)


        navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_prediction -> {
                    // Reset navigasi ke FormFragment
                    navController.popBackStack(R.id.navigation_prediction, false)
                    navController.navigate(R.id.navigation_prediction)
                }

                else -> navController.navigate(menuItem.itemId)
            }
            true
        }
    }
}