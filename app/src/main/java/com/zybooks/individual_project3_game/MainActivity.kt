package com.zybooks.individual_project3_game

import GameMenu
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.zybooks.individual_project3_game.ui.theme.Individual_Project3_GameTheme
import com.zybooks.individual_project3_game.userInterface.ui.screens.MazeGame
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Individual_Project3_GameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Add state management for game/menu
                    var gameState by remember { mutableStateOf("menu") }

                    when (gameState) {
                        "menu" -> {
                            GameMenu(
                                onStartGame = { gameState = "playing" },
                                onExitGame = { finish() }  // This exits the app
                            )
                        }
                        "playing" -> {
                            MazeGame(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}