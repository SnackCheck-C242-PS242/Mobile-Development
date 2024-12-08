package com.snackcheck.view.authorization.verification_success

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.snackcheck.databinding.ActivityVerificationSuccessBinding
import com.snackcheck.view.authorization.login.LoginActivity

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