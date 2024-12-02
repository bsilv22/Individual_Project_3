package com.zybooks.individual_project3_game.Data

import androidx.compose.runtime.mutableStateListOf
import com.yourappname.data.models.Platform
import com.yourappname.data.models.Coin

class Game1Level2 {
    companion object {
        fun getPlatforms(scale: Float) = mutableStateListOf(
            Platform(0f * scale, 80f * scale, 80f * scale, 20f * scale),      // Start platform
            Platform(80f * scale, 60f * scale, 20f * scale, 50f * scale),     // Up connector 1
            Platform(80f * scale, 50f * scale, 80f * scale, 20f * scale),     // High platform 1
            Platform(160f * scale, 50f * scale, 20f * scale, 90f * scale),    // Down connector 1
            Platform(160f * scale, 120f * scale, 80f * scale, 20f * scale),   // Low platform 1
            Platform(240f * scale, 95f * scale, 20f * scale, 80f * scale),   // Up connector 2
            Platform(240f * scale, 80f * scale, 80f * scale, 20f * scale),    // Middle platform
            Platform(320f * scale, 80f * scale, 20f * scale, 100f * scale),   // Down connector 2
            Platform(320f * scale, 150f * scale, 80f * scale, 20f * scale),   // Low platform 2

        )

        fun getCoins(scale: Float) = mutableStateListOf(
            Coin(40f * scale, 90f * scale),      // Start platform
            Coin(120f * scale, 60f * scale),     // High platform 1
            Coin(200f * scale, 130f * scale),    // Low platform 1
            Coin(280f * scale, 90f * scale),     // Middle platform
            Coin(330f * scale, 160f * scale)  // Middle of platform at (320,150,80,20)

        )
    }
}

class Game2Level2 {
    companion object {
        fun getPlatforms(scale: Float) = mutableStateListOf(
            // x = 0
            Platform(0f * scale, 80f * scale, 100f * scale, 20f * scale),     // Start platform

            // x = 100
            Platform(100f * scale, 80f * scale, 20f * scale, 40f * scale),    // Up connector 1
            Platform(100f * scale, 60f * scale, 120f * scale, 20f * scale),   // High platform 1

            // x = 220
            Platform(220f * scale, 50f * scale, 20f * scale, 100f * scale),   // Vertical connector 1
            Platform(220f * scale, 150f * scale, 150f * scale, 20f * scale),  // Low platform 1
            Platform(220f * scale, 180f * scale, 20f * scale, 250f * scale),  // Vertical connector 2

            // x = 240 (connecting platform)
            Platform(240f * scale, 110f * scale, 120f * scale, 20f * scale),  // Connecting platform

            // x = 360
            Platform(360f * scale, 110f * scale, 100f * scale, 20f * scale)   // High platform 2
        )

        fun getCoins(scale: Float) = mutableStateListOf(
            Coin(40f * scale, 90f * scale),     // Start platform
            Coin(150f * scale, 70f * scale),     // High platform 1
            Coin(280f * scale, 160f * scale),    // Low platform 1
            Coin(230f * scale, 120f * scale),    // High platform 2

        )
    }
}

class Game3Level2 {
    companion object {
        fun getPlatforms(scale: Float) = mutableStateListOf(
            Platform(0f * scale, 80f * scale, 100f * scale, 20f * scale),     // Start platform
            Platform(100f * scale, 60f * scale, 20f * scale, 60f * scale),     // Up connector 1
            Platform(100f * scale, 50f * scale, 120f * scale, 20f * scale),    // High platform 1
            Platform(220f * scale, 50f * scale, 20f * scale, 110f * scale),    // Down connector 1
            Platform(220f * scale, 140f * scale, 140f * scale, 20f * scale),   // Low platform 1
            Platform(360f * scale, 120f * scale, 20f * scale, 70f * scale),    // Up connector 2
            Platform(360f * scale, 110f * scale, 120f * scale, 20f * scale),   // High platform 2
            Platform(480f * scale, 80f * scale, 20f * scale, 50f * scale),     // Down connector 3
            Platform(480f * scale, 110f * scale, 100f * scale, 20f * scale)    // Final platform
        )

        fun getCoins(scale: Float) = mutableStateListOf(
            Coin(50f * scale, 90f * scale),     // Start platform
            Coin(160f * scale, 60f * scale),     // High platform 1
            Coin(230f * scale, 115f * scale),    // Middle of long vertical connector
            Coin(290f * scale, 150f * scale),// Low platform 1
            Coin(420f * scale, 120f * scale),    // High platform 2

        )
    }
}