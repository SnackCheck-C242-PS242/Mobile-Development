package com.snackcheck.view.authorization.input_new_password

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityInputNewPasswordBinding
import com.snackcheck.di.Injection
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.login.LoginActivity
import com.snackcheck.view.authorization.verification_success.VerificationSuccessActivity

class InputNewPasswordActivity : AppCompatActivity() {
    private lateinit var viewModel: InputNewPasswordViewModel
    private lateinit var binding: ActivityInputNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref)
        viewModel = ViewModelProvider(this, viewModelFactory)[InputNewPasswordViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnConfirm.setOnClickListener {
                val email = intent.getStringExtra(EXTRA_EMAIL).toString()
                val verificationCode = intent.getStringExtra(EXTRA_VERIFICATION_CODE).toString()

                val newPassword = edPassword.text.toString()
                val newPasswordConfirmation = edPasswordConfirmation.text.toString()

                if (newPassword == newPasswordConfirmation) {
                    viewModel.resetPassword(
                        email = email,
                        resetCode = verificationCode,
                        newPassword = newPassword,
                        confirmPassword = newPasswordConfirmation
                    )
                } else {
                    Toast.makeText(
                        this@InputNewPasswordActivity,
                        getString(R.string.please_fill_the_form_correctly),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.responseResult.observe(this@InputNewPasswordActivity) { response ->
                when (response) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        Toast.makeText(
                            this@InputNewPasswordActivity,
                            getString(R.string.reset_password_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()

                        val intent = Intent(this@InputNewPasswordActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }

                    is ResultState.Error -> {
                        Toast.makeText(
                            this@InputNewPasswordActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_VERIFICATION_CODE = "extra_verification_code"
    }
}