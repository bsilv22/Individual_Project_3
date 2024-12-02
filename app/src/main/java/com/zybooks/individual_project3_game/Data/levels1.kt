package com.zybooks.individual_project3_game.levels

import androidx.compose.runtime.mutableStateListOf
import com.yourappname.data.models.Coin
import com.yourappname.data.models.Platform


class Game1Level1 {
    companion object {
        fun getPlatforms(scale: Float) = mutableStateListOf(
            Platform(0f * scale, 80f * scale, 80f * scale, 20f * scale),
            Platform(80f * scale, 60f * scale, 20f * scale, 40f * scale),
            Platform(100f * scale, 60f * scale, 80f * scale, 20f * scale),
            Platform(180f * scale, 60f * scale, 20f * scale, 60f * scale),
            Platform(200f * scale, 100f * scale, 80f * scale, 20f * scale),
            Platform(280f * scale, 80f * scale, 20f * scale, 40f * scale),
            Platform(300f * scale, 80f * scale, 80f * scale, 20f * scale),
            Platform(380f * scale, 80f * scale, 20f * scale, 60f * scale),
            Platform(400f * scale, 120f * scale, 80f * scale, 20f * scale),
            Platform(480f * scale, 100f * scale, 20f * scale, 40f * scale),
            Platform(500f * scale, 100f * scale, 80f * scale, 20f * scale),
            Platform(580f * scale, 100f * scale, 20f * scale, 60f * scale),
            Platform(600f * scale, 140f * scale, 80f * scale, 20f * scale),
        )

        fun getCoins(scale: Float) = mutableStateListOf(
            Coin(40f * scale, 90f * scale),    // First platform
            Coin(140f * scale, 70f * scale),   // Third platform
            Coin(240f * scale, 110f * scale),  // Fifth platform
            Coin(340f * scale, 90f * scale)    // Seventh platform
        )
    }
}

class Game2Level1 {
    companion object {
        fun getPlatforms(scale: Float) = mutableStateListOf(
            Platform(0f * scale, 80f * scale, 100f * scale, 20f * scale),
            Platform(100f * scale, 60f * scale, 20f * scale, 60f * scale),
            Platform(120f * scale, 60f * scale, 100f * scale, 20f * scale),
            Platform(220f * scale, 60f * scale, 20f * scale, 80f * scale),
            Platform(240f * scale, 120f * scale, 100f * scale, 20f * scale),
            Platform(340f * scale, 90f * scale, 20f * scale, 50f * scale),
            Platform(360f * scale, 90f * scale, 100f * scale, 20f * scale),
        )

        fun getCoins(scale: Float) = mutableStateListOf(
            Coin(50f * scale, 90f * scale),     // Center of first platform (0 + 100/2)
            Coin(170f * scale, 70f * scale),    // Center of third platform (120 + 100/2)
            Coin(290f * scale, 130f * scale),   // Center of fifth platform (240 + 100/2)
            Coin(410f * scale, 100f * scale)    // Center of seventh platform (360 + 100/2)
        )
    }
}

class Game3Level1 {
    companion object {
        fun getPlatforms(scale: Float) = mutableStateListOf(
            Platform(0f * scale, 80f * scale, 80f * scale, 20f * scale),     // Starting platform
            Platform(80f * scale, 80f * scale, 20f * scale, 40f * scale),    // Connects to third
            Platform(100f * scale, 100f * scale, 80f * scale, 20f * scale),  // High platform
            Platform(180f * scale, 100f * scale, 20f * scale, 60f * scale),  // Connects to fifth
            Platform(200f * scale, 140f * scale, 80f * scale, 20f * scale),  // Higher
            Platform(280f * scale, 140f * scale, 20f * scale, 100f * scale), // Long vertical connector

        )

        fun getCoins(scale: Float) = mutableStateListOf(
            Coin(40f * scale, 90f * scale),        // First platform
            Coin(140f * scale, 110f * scale),      // Third platform
            Coin(240f * scale, 150f * scale),      // Fifth platform
        )
    }
}