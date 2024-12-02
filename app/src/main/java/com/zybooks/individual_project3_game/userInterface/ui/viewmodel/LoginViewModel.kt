package com.zybooks.individual_project3_game.userInterface.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zybooks.individual_project3_game.GameDatabase
import com.zybooks.individual_project3_game.Kid
import com.zybooks.individual_project3_game.Parent

class LoginViewModel(private val context: Context) : ViewModel() {
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val database = GameDatabase(context)

    fun login(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) {
            _errorMessage.value = "Username and password are required"
            return false
        }

        // Try parent login first
        val parent = database.getParentByUsername(username)
        if (parent != null) {
            if (parent.password == password) {
                _errorMessage.value = null
                // Save logged in state if needed
                return true
            }
        }

        // Try kid login
        val kid = database.getKidByUsername(username)
        if (kid != null) {
            if (kid.password == password) {
                _errorMessage.value = null
                // Save logged in state if needed
                return true
            }
        }

        _errorMessage.value = "Invalid username or password"
        return false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}