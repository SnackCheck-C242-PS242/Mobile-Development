package com.snackcheck.view.authorization.register

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
import com.snackcheck.databinding.ActivitySignUpBinding
import com.snackcheck.di.Injection
import com.snackcheck.view.ViewModelFactory
import com.snackcheck.view.authorization.login.LoginActivity
import com.snackcheck.view.authorization.verification.VerificationActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            tbLogin.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }

            btnSignup.setOnClickListener{
                if (edFullName.text!!.isNotEmpty() && edUsername.text!!.isNotEmpty() && edEmail.text!!.isNotEmpty() && edPassword.text?.length!! >= 8 && (edPasswordConfirmation.text!!.toString() == edPassword.text.toString())){
                    viewModel.register(
                        fullName = edFullName.text.toString(),
                        username = edUsername.text.toString(),
                        email = edEmail.text.toString(),
                        password = edPassword.text.toString(),
                        confirmPassword = edPasswordConfirmation.text.toString()
                    )
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.please_fill_the_form_correctly),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.responseResult.observe(this@SignUpActivity) { response ->
                when (response) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        Toast.makeText(
                            this@SignUpActivity,
                            getString(R.string.register_success),
                            Toast.LENGTH_SHORT
                            ).show()

                        val email = edEmail.text.toString()
                        val intent = Intent(this@SignUpActivity, VerificationActivity::class.java)
                        intent.putExtra(VerificationActivity.EXTRA_EMAIL, email)
                        startActivity(intent)
                        finish()
                    }

                    is ResultState.Error -> {
                        Toast.makeText(
                            this@SignUpActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}