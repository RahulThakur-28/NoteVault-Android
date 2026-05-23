package com.example.rahul.thenotesapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.rahul.thenotesapp.MainActivity
import com.example.rahul.thenotesapp.R
import com.example.rahul.thenotesapp.ui.auth.LoginActivity
import com.example.rahul.thenotesapp.utils.PreferenceManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ THIS LINE WAS MISSING
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val pref = PreferenceManager(this)

            if (pref.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            finish()
        }, 2000)
    }
}