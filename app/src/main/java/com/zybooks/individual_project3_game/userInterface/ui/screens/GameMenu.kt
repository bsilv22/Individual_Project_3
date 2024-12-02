// Create this as MainMenu.kt
package com.zybooks.individual_project3_game.userInterface.ui.screens

import android.content.Context
import android.media.SoundPool
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zybooks.individual_project3_game.R

@Composable
fun MainMenu(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onExitGame: () -> Unit) {

    val context = LocalContext.current
    val soundHelper = remember { SoundHelper(context) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E88E5),  // Light blue
            Color(0xFF0D47A1)   // Dark blue
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PLATFORM\n\n\nGAME",
            fontSize = 56.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                soundHelper.playClickSound()
                onLoginClick()
            },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 240.dp, height = 64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "LOGIN",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                soundHelper.playClickSound()
                onRegisterClick()
            },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 240.dp, height = 64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "REGISTER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                soundHelper.playClickSound()
                onExitGame()
            },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 240.dp, height = 64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "EXIT",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Keep your existing SoundHelper class
class SoundHelper(context: Context) {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .build()

    private val buttonClickSound = soundPool.load(context, R.raw.button_click, 1)

    fun playClickSound() {
        soundPool.play(buttonClickSound, 10f, 10f, 5, 0, 1f)
    }
}