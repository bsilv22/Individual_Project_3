package com.zybooks.individual_project3_game

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Individual_Project3_GameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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