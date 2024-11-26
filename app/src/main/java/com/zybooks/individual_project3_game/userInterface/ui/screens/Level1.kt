package com.zybooks.individual_project3_game.levels

import androidx.compose.runtime.mutableStateListOf
import com.zybooks.individual_project3_game.userInterface.ui.screens.Coin
import com.zybooks.individual_project3_game.userInterface.ui.screens.Platform

class Level1 {
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
            Coin(40f * scale, 90f * scale),
            Coin(140f * scale, 70f * scale),
            Coin(240f * scale, 110f * scale),
            Coin(340f * scale, 90f * scale),
        )
    }
}