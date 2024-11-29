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
import com.snackcheck.databinding.ActivityLoginBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.register.SignUpActivity
import com.snackcheck.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupAction()
        setupPasswordValidation()

        viewModel.passwordError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.passwordEditText.error = errorMessage
                binding.passwordEditTextLayout.isErrorEnabled = true
                binding.passwordEditTextLayout.errorIconDrawable = null
            } else {
                binding.passwordEditTextLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupAction() {
        binding.tbSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            viewModel.saveSession(UserModel(username, "sample_token"))
            AlertDialog.Builder(this).apply {
                setTitle("Login Success")
                setMessage("You have logged in successfully.")
                setPositiveButton("OK") { _, _ ->
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }.show()
        }
    }

    private fun setupPasswordValidation() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    viewModel.setPasswordError("Password must be at least 8 characters long")
                } else {
                    viewModel.setPasswordError(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}