package com.zybooks.individual_project3_game.userInterface.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zybooks.individual_project3_game.GameDatabase

class RegisterViewModel(private val context: Context) : ViewModel() {
    private val _formState = mutableStateOf(mutableMapOf<String, String>())
    val formState: State<Map<String, String>> = _formState

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val database = GameDatabase(context)

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun updateForm(key: String, value: String) {
        _formState.value = _formState.value.toMutableMap().apply {
            put(key, value)
        }
    }

    fun register(formData: Map<String, String>, isParent: Boolean): Boolean {
        _errorMessage.value = null

        val username = formData["username"]
        val email = formData["email"]
        val password = formData["password"]

        // Basic validation
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            _errorMessage.value = "Username and password are required"
            return false
        }

        if (isParent) {
            if (email.isNullOrBlank()) {
                _errorMessage.value = "Email is required for parent accounts"
                return false
            }

            if (!isValidEmail(email)) {
                _errorMessage.value = "Please enter a valid email address"
                return false
            }
        }

        try {
            if (isParent) {
                // Check if parent username already exists
                if (database.getParentByUsername(username) != null) {
                    _errorMessage.value = "Username already exists"
                    return false
                }

                // Create parent account
                val parentId = database.createParentAccount(
                    username = username,
                    password = password,
                    email = email ?: ""
                )

                if (parentId == -1L) {
                    _errorMessage.value = "Failed to create account"
                    return false
                }

            } else {
                // Rest of the kid registration logic remains the same...
                val parentCode = formData["parentCode"]
                if (parentCode.isNullOrBlank()) {
                    _errorMessage.value = "Parent code is required"
                    return false
                }

                if (database.getKidByUsername(username) != null) {
                    _errorMessage.value = "Username already exists"
                    return false
                }

                val parent = database.getParentByUsername(parentCode)
                if (parent == null) {
                    _errorMessage.value = "Invalid parent code"
                    return false
                }

                val kidId = database.createKidAccount(
                    username = username,
                    password = password,
                    age = 0,
                    parentId = parent.id
                )

                if (kidId == -1L) {
                    _errorMessage.value = "Failed to create account"
                    return false
                }
            }

            return true
        } catch (e: Exception) {
            _errorMessage.value = "An error occurred: ${e.message}"
            return false
        }
    }
}