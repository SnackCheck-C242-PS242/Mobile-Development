package com.snackcheck.view.authorization.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.snackcheck.data.pref.UserModel
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityLoginBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.register.SignUpActivity
import com.snackcheck.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val pref = UserPreference.getInstance(dataStore)
    private val factory = ViewModelFactory.getInstance(this, pref)
    private val viewModel by viewModels<LoginViewModel> {
        factory
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.tbSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


    }

}