package com.snackcheck.view.authorization.reset_password

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
import com.snackcheck.databinding.ActivityResetPasswordBinding
import com.snackcheck.di.Injection
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.verification.VerificationActivity

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ResetPasswordViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnSendCode.setOnClickListener {
                if (tlEmail.error.isNullOrEmpty()) {
                    viewModel.getResetCode(
                        email = edEmail.text.toString()
                    )
                }
            }

            val builder: AlertDialog.Builder =
                MaterialAlertDialogBuilder(
                    this@ResetPasswordActivity,
                    R.style.MaterialAlertDialog_Rounded
                )
            builder.setView(R.layout.layout_loading)
            val dialog: AlertDialog = builder.create()

            viewModel.responseResult.observe(this@ResetPasswordActivity) { response ->
                when (response) {
                    is ResultState.Loading -> dialog.show()
                    is ResultState.Success -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            getString(R.string.verification_succes),
                            Toast.LENGTH_SHORT
                        ).show()

                        val email = edEmail.text.toString()
                        val intent =
                            Intent(this@ResetPasswordActivity, VerificationActivity::class.java)
                        intent.putExtra(VerificationActivity.FORGOT_PASSWORD, true)
                        intent.putExtra(VerificationActivity.EXTRA_EMAIL, email)
                        startActivity(intent)
                        finish()
                    }

                    is ResultState.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@ResetPasswordActivity,
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