package com.example.rahul.thenotesapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rahul.thenotesapp.MainActivity
import com.example.rahul.thenotesapp.databinding.ActivityLoginBinding
import com.example.rahul.thenotesapp.utils.PreferenceManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        // Auto-login
        if (preferenceManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Login button
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedEmail = preferenceManager.getUserEmail()
            val savedPassword = preferenceManager.getUserPassword()

            if (email == savedEmail && password == savedPassword) {
                preferenceManager.setLoggedIn(true)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // Go to Signup
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}