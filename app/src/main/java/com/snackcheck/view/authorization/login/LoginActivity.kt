package com.snackcheck.view.authorization.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityLoginBinding
import com.snackcheck.di.Injection
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.register.SignUpActivity
import com.snackcheck.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            tbSignUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnLogin.setOnClickListener {
                if (edUsername.text!!.isNotEmpty() && edPassword.text?.length!! >= 8) {
                    viewModel.login(
                        username = edUsername.text.toString(),
                        password = edPassword.text.toString()
                    )
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.please_fill_the_form_correctly),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val builder: AlertDialog.Builder =
                MaterialAlertDialogBuilder(this@LoginActivity, R.style.MaterialAlertDialog_Rounded)
            builder.setView(R.layout.layout_loading)
            val dialog: AlertDialog = builder.create()

            viewModel.responseResult.observe(this@LoginActivity) { response ->
                when (response) {
                    is ResultState.Loading -> dialog.show()
                    is ResultState.Success -> {
                        dialog.dismiss()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    is ResultState.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> dialog.dismiss()
                }
            }
        }
    }
}