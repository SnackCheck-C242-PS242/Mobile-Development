package com.snackcheck.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snackcheck.R
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityMainBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val pref = UserPreference.getInstance(dataStore)
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    private val viewModel by viewModels<MainViewModel> {
        factory
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navView: BottomNavigationView = binding.navView

        // Pastikan NavHostFragment ditemukan
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Hubungkan Bottom Navigation dengan NavController
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