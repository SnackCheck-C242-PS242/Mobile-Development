package com.snackcheck.view.authorization.verification

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import com.snackcheck.databinding.ActivityVerificationBinding
import com.snackcheck.di.Injection
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.input_new_password.InputNewPasswordActivity
import com.snackcheck.view.authorization.verification_success.VerificationSuccessActivity

class VerificationActivity : AppCompatActivity() {

    private lateinit var viewModel: VerificationViewModel
    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[VerificationViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupOtpFocus()
        setupAction()
    }

    private fun setupAction() {
        val isForgotPassword = intent.getBooleanExtra(FORGOT_PASSWORD, false)
        val email = intent.getStringExtra(EXTRA_EMAIL).toString()

        binding.apply {
            if (isForgotPassword) {
                btnVerifySignup.visibility = View.GONE
                btnVerifyForgotPassword.visibility = View.VISIBLE
                btnVerifyForgotPassword.setOnClickListener {
                    val otp1 = edOtp1.text.toString()
                    val otp2 = edOtp2.text.toString()
                    val otp3 = edOtp3.text.toString()
                    val otp4 = edOtp4.text.toString()
                    val otp5 = edOtp5.text.toString()

                    if (otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() || otp4.isEmpty() || otp5.isEmpty()) {
                        Toast.makeText(
                            this@VerificationActivity,
                            getString(R.string.please_fill_in_all_otp_fields),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    val verificationCode = "$otp1$otp2$otp3$otp4$otp5"
                    viewModel.verifyResetCode(email, verificationCode)
                }
            } else {
                btnVerifySignup.visibility = View.VISIBLE
                btnVerifyForgotPassword.visibility = View.GONE
                btnVerifySignup.setOnClickListener {

                    val otp1 = edOtp1.text.toString()
                    val otp2 = edOtp2.text.toString()
                    val otp3 = edOtp3.text.toString()
                    val otp4 = edOtp4.text.toString()
                    val otp5 = edOtp5.text.toString()

                    if (otp1.isEmpty() || otp2.isEmpty() || otp3.isEmpty() || otp4.isEmpty() || otp5.isEmpty()) {
                        Toast.makeText(
                            this@VerificationActivity,
                            getString(R.string.please_fill_in_all_otp_fields),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    val verificationCode = "$otp1$otp2$otp3$otp4$otp5"
                    viewModel.verify(email, verificationCode)
                }
            }


        }

        val builder: AlertDialog.Builder =
            MaterialAlertDialogBuilder(
                this@VerificationActivity,
                R.style.MaterialAlertDialog_Rounded
            )
        builder.setView(R.layout.layout_loading)
        val dialog: AlertDialog = builder.create()

        viewModel.resetResponseResult.observe(this) { response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Success -> {
                    dialog.dismiss()
                    val intent =
                        Intent(this@VerificationActivity, InputNewPasswordActivity::class.java)
                    intent.putExtra(InputNewPasswordActivity.EXTRA_EMAIL, email)
                    startActivity(intent)
                }

                is ResultState.Error -> {
                    dialog.dismiss()
                    Toast.makeText(
                        this@VerificationActivity,
                        response.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        viewModel.responseResult.observe(this) { response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Success -> {
                    dialog.dismiss()
                    val intent =
                        Intent(this@VerificationActivity, VerificationSuccessActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is ResultState.Error -> {
                    dialog.dismiss()
                    Toast.makeText(
                        this@VerificationActivity,
                        response.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupOtpFocus() {
        binding.apply {
            edOtp1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    if (edOtp1.text?.length == 1) {
                        edOtp2.requestFocus()
                    }
                }

                override fun afterTextChanged(editable: Editable?) {}
            })

            edOtp2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    if (edOtp2.text?.length == 1) {
                        edOtp3.requestFocus()
                    }
                }

                override fun afterTextChanged(editable: Editable?) {}
            })

            edOtp3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    if (edOtp3.text?.length == 1) {
                        edOtp4.requestFocus()
                    }
                }

                override fun afterTextChanged(editable: Editable?) {}
            })

            edOtp4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    if (edOtp4.text?.length == 1) {
                        edOtp5.requestFocus()
                    }
                }

                override fun afterTextChanged(editable: Editable?) {}
            })

            edOtp5.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                }

                override fun afterTextChanged(editable: Editable?) {}
            })
        }
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
        const val FORGOT_PASSWORD = "is_forgot_password"
    }
}