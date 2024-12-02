package com.zybooks.individual_project3_game.userInterface.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
    fun DirectionalArrow(
        direction: String,
        modifier: Modifier = Modifier
    ) {

        Canvas(modifier = modifier.size(40.dp)) {
            val width = size.width
            val height = size.height
            val path = androidx.compose.ui.graphics.Path()

            when (direction) {
                "up" -> {
                    // Arrow shaft
                    path.moveTo(width * 0.5f, height * 0.8f)
                    path.lineTo(width * 0.5f, height * 0.3f)

                    // Arrow head with curves
                    path.moveTo(width * 0.2f, height * 0.5f)
                    path.quadraticBezierTo(
                        width * 0.5f, height * 0.1f,
                        width * 0.8f, height * 0.5f
                    )

                    drawPath(
                        path = path,
                        color = Color(0xFF4CAF50),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 8f,
                            cap = androidx.compose.ui.graphics.StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round
                        )
                    )
                }
                "down" -> {
                    // Arrow shaft
                    path.moveTo(width * 0.5f, height * 0.2f)
                    path.lineTo(width * 0.5f, height * 0.7f)

                    // Arrow head with curves
                    path.moveTo(width * 0.2f, height * 0.5f)
                    path.quadraticBezierTo(
                        width * 0.5f, height * 0.9f,
                        width * 0.8f, height * 0.5f
                    )

                    drawPath(
                        path = path,
                        color = Color(0xFFE91E63),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 8f,
                            cap = androidx.compose.ui.graphics.StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round
                        )
                    )
                }
                "right" -> {
                    // Arrow shaft
                    path.moveTo(width * 0.2f, height * 0.5f)
                    path.lineTo(width * 0.7f, height * 0.5f)

                    // Arrow head with curves
                    path.moveTo(width * 0.5f, height * 0.2f)
                    path.quadraticBezierTo(
                        width * 0.9f, height * 0.5f,
                        width * 0.5f, height * 0.8f
                    )

                    drawPath(
                        path = path,
                        color = Color(0xFF2196F3),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 8f,
                            cap = androidx.compose.ui.graphics.StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round
                        )
                    )
                }
            }
        }
    }
