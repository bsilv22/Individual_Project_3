package com.zybooks.individual_project3_game.userInterface.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.individual_project3_game.userInterface.ui.viewmodel.RegisterViewModel
import com.zybooks.individual_project3_game.userInterface.ui.viewmodel.RegisterViewModelFactory

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(context)
    )

    var showForm by remember { mutableStateOf(false) }
    var registrationType by remember { mutableStateOf<String?>(null) }

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
        if (!showForm) {
            Text(
                text = "Register As",
                fontSize = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = TextStyle(textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    registrationType = "parent"
                    showForm = true
                },
                modifier = Modifier
                    .padding(8.dp)
                    .size(width = 240.dp, height = 64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(
                    text = "PARENT",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }

            Button(
                onClick = {
                    registrationType = "kid"
                    showForm = true
                },
                modifier = Modifier
                    .padding(8.dp)
                    .size(width = 240.dp, height = 64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text(
                    text = "KID",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .padding(8.dp)
                    .size(width = 240.dp, height = 64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                )
            ) {
                Text(
                    text = "BACK",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (registrationType == "parent") "Parent Registration" else "Kid Registration",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var username by remember { mutableStateOf("") }
                    var email by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    var parentCode by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username", style = TextStyle(fontSize = 14.sp)) },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", style = TextStyle(fontSize = 14.sp)) },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", style = TextStyle(fontSize = 14.sp)) },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (registrationType == "kid") {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = parentCode,
                            onValueChange = { parentCode = it },
                            label = { Text("Parent Code", style = TextStyle(fontSize = 14.sp)) },
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.updateForm("username", username)
                            viewModel.updateForm("email", email)
                            viewModel.updateForm("password", password)
                            if (registrationType == "kid") {
                                viewModel.updateForm("parentCode", parentCode)
                            }
                            if (viewModel.register(
                                    viewModel.formState.value,
                                    registrationType == "parent"
                                )
                            ) {
                                onRegisterSuccess()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Register",
                            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
                        )
                    }

                    TextButton(
                        onClick = { showForm = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Back",
                            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
                        )
                    }

                    viewModel.errorMessage.value?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                }
            }
        }
    }
}