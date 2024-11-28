package com.yourappname.data.models

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
