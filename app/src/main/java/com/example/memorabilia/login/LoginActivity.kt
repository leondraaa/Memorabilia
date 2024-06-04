package com.example.memorabilia.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.data.UserModel
import com.example.memorabilia.databinding.ActivityLoginBinding
import com.example.memorabilia.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.login(email, password) { response ->
                if (response != null && !response.error) {
                    alertDialog = AlertDialog.Builder(this)
                        .setTitle("Login Successful")
                        .setMessage("You have successfully logged in.")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()

                    viewModel.updateToken(response.loginResult.token)
                    val user = UserModel(email, response.loginResult.token, true)
                    viewModel.saveSession(user)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    alertDialog = AlertDialog.Builder(this)
                        .setTitle("Login Failed")
                        .setMessage("Login failed. Please try again.")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        alertDialog?.dismiss()
        super.onDestroy()
    }

    }
