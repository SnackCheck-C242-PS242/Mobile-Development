package com.snackcheck.view.authorization.verification_success

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snackcheck.R
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityVerificationBinding
import com.snackcheck.databinding.ActivityVerificationSuccessBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.login.LoginActivity
import com.snackcheck.view.authorization.verification.VerificationViewModel

class VerificationSuccessActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVerificationSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVerificationSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnGoToLogin.setOnClickListener {
                val intent = Intent(this@VerificationSuccessActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}