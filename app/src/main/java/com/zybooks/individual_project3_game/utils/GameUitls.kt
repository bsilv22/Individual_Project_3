package com.zybooks.individual_project3_game.userInterface.ui



// GameComponents.kt
data class Platform(
    val x: Float,
    val y: Float,
    val width: Float = 50f,
    val height: Float = 50f
)

data class Coin(
    val x: Float,
    val y: Float,
    var collected: Boolean = false
)

fun isOnPlatform(x: Float, y: Float, platforms: List<Platform>): Boolean {
    val playerRadius = 50f  // Match circle radius

    // First check for connected platforms (allow movement between them)
    val connectedPlatforms = platforms.filter { platform ->
        // Include current platform and adjacent platforms
        x >= platform.x - playerRadius &&
                x <= platform.x + platform.width + playerRadius &&
                y >= platform.y - playerRadius &&
                y <= platform.y + platform.height + playerRadius
    }

    // If we have multiple connected platforms, treat them as one larger platform
    if (connectedPlatforms.size > 1) {
        val minX = connectedPlatforms.minOf { it.x }
        val maxX = connectedPlatforms.maxOf { it.x + it.width }
        val minY = connectedPlatforms.minOf { it.y }
        val maxY = connectedPlatforms.maxOf { it.y + it.height }

        return x >= minX + playerRadius &&
                x <= maxX - playerRadius &&
                y >= minY + playerRadius &&
                y <= maxY - playerRadius
    }

    // Otherwise check single platform bounds
    return platforms.any { platform ->
        x >= platform.x + playerRadius &&
                x <= platform.x + platform.width - playerRadius &&
                y >= platform.y + playerRadius &&
                y <= platform.y + platform.height - playerRadius
    }
}