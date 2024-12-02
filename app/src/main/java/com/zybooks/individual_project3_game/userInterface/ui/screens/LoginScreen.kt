package com.zybooks.individual_project3_game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.individual_project3_game.userInterface.ui.viewmodel.LoginViewModel
import com.zybooks.individual_project3_game.userInterface.ui.viewmodel.LoginViewModelFactory

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(LocalContext.current)
    )

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 40.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = {
                username = it
                viewModel.clearError()
            },
            label = { Text("Username", color = Color.Black) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            modifier = Modifier
                .width(240.dp)
                .heightIn(min = 56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError()
            },
            label = { Text("Password", color = Color.Black) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            modifier = Modifier
                .width(240.dp)
                .heightIn(min = 56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if any
        viewModel.errorMessage.value?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.login(username, password)) {
                    onLoginSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("LOGIN")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
        ) {
            Text("BACK")
        }
    }
}