package com.zybooks.individual_project3_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zybooks.individual_project3_game.ui.theme.Individual_Project3_GameTheme
import com.zybooks.individual_project3_game.userInterface.ui.screens.MazeGame
import android.app.Activity
import androidx.navigation.NavController
import com.zybooks.individual_project3_game.userInterface.ui.screens.RegisterScreen

sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object Login : Screen("login")
    object Register : Screen("register")
    object Game : Screen("game")
}

class MainActivity : ComponentActivity() {
    private lateinit var musicController: BackgroundMusicController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicController = BackgroundMusicController(this)

        enableEdgeToEdge()
        setContent {
            Individual_Project3_GameTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.MainMenu.route
                        ) {
                            composable(Screen.MainMenu.route) {
                                com.zybooks.individual_project3_game.userInterface.ui.screens.MainMenu(
                                    onLoginClick = { navController.navigate(Screen.Login.route) },
                                    onRegisterClick = { navController.navigate(Screen.Register.route) },
                                    onExitGame = { (context as? Activity)?.finish() }
                                )
                            }

                            composable(Screen.Login.route) {
                                LoginScreen(
                                    onLoginSuccess = {
                                        navController.navigate(Screen.Game.route) {
                                            popUpTo(Screen.MainMenu.route) { inclusive = true }
                                        }
                                    },
                                    onBack = { navController.navigateUp() }
                                )
                            }

                            composable(Screen.Register.route) {
                                RegisterScreen(
                                    onRegisterSuccess = {
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Register.route) { inclusive = true }
                                        }
                                    },
                                    onBack = { navController.navigateUp() }
                                )
                            }

                            composable(Screen.Game.route) {
                                MazeGame(
                                    playerName = "Player 1",
                                    navController = navController,
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

    override fun onResume() {
        super.onResume()
        musicController.resumeMusic()
    }

    override fun onPause() {
        super.onPause()
        musicController.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicController.stopMusic()
    }
}