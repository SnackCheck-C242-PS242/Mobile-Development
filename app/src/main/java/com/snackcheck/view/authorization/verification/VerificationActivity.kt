package com.snackcheck.view.authorization.verification

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.pref.dataStore
import com.snackcheck.databinding.ActivityVerificationBinding
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.verification_success.VerificationSuccessActivity

class VerificationActivity : AppCompatActivity() {
    private val pref = UserPreference.getInstance(dataStore)
    private lateinit var binding: ActivityVerificationBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    private val viewModel: VerificationViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
    }

    private fun setupAction(){
        binding.apply {
            btnVerify.setOnClickListener {
                val email = intent.getStringExtra(EXTRA_EMAIL).toString()

                val otp1 = edOtp1.text.toString()
                val otp2 = edOtp2.text.toString()
                val otp3 = edOtp3.text.toString()
                val otp4 = edOtp4.text.toString()
                val otp5 = edOtp5.text.toString()

                val verificationCode = "$otp1$otp2$otp3$otp4$otp5"

                viewModel.verify(email, verificationCode)
            }
        }

        val builder: AlertDialog.Builder =
            MaterialAlertDialogBuilder(this@VerificationActivity, R.style.MaterialAlertDialog_Rounded)
        builder.setView(R.layout.layout_loading)
        val dialog: AlertDialog = builder.create()

        viewModel.responseResult.observe(this){ response ->
            when(response){
                is ResultState.Loading -> dialog.show()
                is ResultState.Success -> {
                    dialog.dismiss()
                    val intent = Intent(this@VerificationActivity, VerificationSuccessActivity::class.java)
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


    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }
}