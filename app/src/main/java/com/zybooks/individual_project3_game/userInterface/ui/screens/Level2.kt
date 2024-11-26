package com.zybooks.individual_project3_game.levels

import androidx.compose.runtime.mutableStateListOf
import com.zybooks.individual_project3_game.userInterface.ui.screens.Coin
import com.zybooks.individual_project3_game.userInterface.ui.screens.Platform

class Level2 {
    companion object {
        fun getPlatforms(scale: Float) = mutableStateListOf(
            Platform(0f * scale, 80f * scale, 100f * scale, 20f * scale),
            Platform(100f * scale, 80f * scale, 20f * scale, 60f * scale),
            Platform(120f * scale, 120f * scale, 100f * scale, 20f * scale),
            Platform(220f * scale, 100f * scale, 20f * scale, 40f * scale),
            Platform(240f * scale, 100f * scale, 100f * scale, 20f * scale),
            Platform(340f * scale, 80f * scale, 20f * scale, 40f * scale),
            Platform(360f * scale, 80f * scale, 100f * scale, 20f * scale),
            Platform(460f * scale, 60f * scale, 20f * scale, 40f * scale),
            Platform(480f * scale, 60f * scale, 100f * scale, 20f * scale),
            Platform(580f * scale, 60f * scale, 20f * scale, 80f * scale),
            Platform(600f * scale, 120f * scale, 100f * scale, 20f * scale),
            Platform(700f * scale, 100f * scale, 20f * scale, 40f * scale),
            Platform(720f * scale, 100f * scale, 100f * scale, 20f * scale),
        )

        fun getCoins(scale: Float) = mutableStateListOf(
            Coin(50f * scale, 90f * scale),    // Adjusted Y positions
            Coin(170f * scale, 70f * scale),
            Coin(290f * scale, 110f * scale),
            Coin(410f * scale, 90f * scale),
            Coin(530f * scale, 130f * scale),
            Coin(650f * scale, 110f * scale),
            Coin(770f * scale, 150f * scale)
        )
    }
}