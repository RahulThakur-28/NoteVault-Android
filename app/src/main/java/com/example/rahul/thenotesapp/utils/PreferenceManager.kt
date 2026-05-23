package com.example.rahul.thenotesapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("NoteAppPrefs", Context.MODE_PRIVATE)

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun saveUser(email: String, password: String) {
        sharedPreferences.edit().putString("email", email).apply()
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun getUserEmail(): String? = sharedPreferences.getString("email", null)
    fun getUserPassword(): String? = sharedPreferences.getString("password", null)

    fun logout() {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
    }
}