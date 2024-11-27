@file:OptIn(ExperimentalFoundationApi::class)
package com.zybooks.individual_project3_game.userInterface.ui.screens



import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.delay
import kotlin.math.abs
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import com.zybooks.individual_project3_game.R
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.core.content.res.ResourcesCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import com.zybooks.individual_project3_game.levels.Level1
import com.zybooks.individual_project3_game.levels.Level2
import java.time.format.TextStyle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale

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

@OptIn(ExperimentalFoundationApi::class)
@Composable

fun MazeGame(modifier: Modifier = Modifier) {
    var playerX by remember { mutableStateOf(10f * 6f) }
    var playerY by remember { mutableStateOf((80f + (20f / 2)) * 6f) }// 90f * scale
    var direction by remember { mutableStateOf("none") }

    var showLevelTransition by remember { mutableStateOf(false) }

    val scale = 6f


    val context = LocalContext.current
    val coinBitmap = remember(context) {
        BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin4)
            ?.let { originalBitmap ->
                Bitmap.createScaledBitmap(originalBitmap, 160, 160, true)
            }
    }

    //next level code
    var currentLevel by remember { mutableStateOf(2) }

    var platforms by remember(currentLevel) {
        mutableStateOf(
            when (currentLevel) {
                1 -> Level1.getPlatforms(scale)
                2 -> Level2.getPlatforms(scale)
                else -> Level1.getPlatforms(scale)
            }
        )
    }

    var coins by remember(currentLevel) {
        val coinsList = when (currentLevel) {
            1 -> Level1.getCoins(scale)
            2 -> Level2.getCoins(scale)
            else -> Level1.getCoins(scale)
        }
        mutableStateOf(coinsList)
    }

    var score by remember { mutableStateOf(0) }

// Add debug logging and modify level change logic
    LaunchedEffect(score) {
        val coinsInLevel = coins.size
        Log.d("GameDebug", "Score: $score, Coins in level: $coinsInLevel")

        if (score > 0 && score == coinsInLevel) {  // Changed condition
            Log.d("GameDebug", "Level complete! Moving to level ${currentLevel + 1}")
            currentLevel++
            score = 0  // Reset score for new level
            playerX = 30f * scale
            playerY = (80f + (20f / 2)) * scale
            direction = "none"

            // Reset coin collection status

        }
    }


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

    var totalScore by remember { mutableStateOf(0) }


    LaunchedEffect(direction) {
        when (direction) {
            "up" -> {
                var velocity = 8f
                repeat(90) {
                    delay(16)
                    val newY = playerY - velocity
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                        coins.forEach { coin ->
                            if (!coin.collected && abs(playerX - coin.x) < 55f && abs(playerY - coin.y) < 55f) {
                                coin.collected = true
                                score += 1
                                totalScore += 1  // Add this line
                            }
                        }
                    } else {
                        velocity *= 0.9f  // Slow down near edges
                    }
                }
            }

            "down" -> {
                var velocity = 8f
                repeat(90) {
                    delay(16)
                    val newY = playerY + velocity
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                        coins.forEach { coin ->
                            if (!coin.collected && abs(playerX - coin.x) < 55f && abs(playerY - coin.y) < 55f) {
                                coin.collected = true
                                score += 1
                                totalScore += 1  // Add this line
                            }
                        }
                    } else {
                        velocity *= 0.9f  // Slow down near edges
                    }
                }
            }

            "right" -> {
                var velocity = 8f
                repeat(90) {
                    delay(16)
                    val newX = playerX + velocity
                    if (isOnPlatform(newX, playerY, platforms)) {
                        playerX = newX
                        coins.forEach { coin ->
                            if (!coin.collected && abs(playerX - coin.x) < 55f && abs(playerY - coin.y) < 55f) {
                                coin.collected = true
                                score += 1
                                totalScore += 1  // Add this line
                            }
                        }
                    } else {
                        velocity *= 0.9f  // Slow down near edges
                    }
                }
            }
        }
        direction = "none"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.winter_background_top),
                contentDescription = "Winter Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly // Add this
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly // Add this
                ) {
                    // Up Button
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp)
                            .border(1.dp, Color.Black)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            direction = "up"

                                            Log.d("MazeGame", "Direction changed to: $direction")

                                            return true
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Up",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            DirectionalArrow(direction = "up")
                        }
                    }
                    // Down Button
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp)
                            .border(1.dp, Color.Black)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            direction = "down"
                                            return true
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Down",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            DirectionalArrow(direction = "down")
                        }
                    }

                    // Right Button
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp)
                            .border(1.dp, Color.Black)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {

                                            direction = "right"
                                            return true
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Right",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            DirectionalArrow(direction = "right")
                        }
                    }
                }

                // Star Box
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        var isDragging by remember { mutableStateOf(false) }

                        val scale by animateFloatAsState(
                            targetValue = if (isDragging) 1.2f else 1f,
                            label = "drag scale"
                        )

                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(150.dp)
                                .scale(scale)
                                .alpha(
                                    animateFloatAsState(
                                        targetValue = if (isDragging) 0.4f else 1f,
                                        label = "fade animation"
                                    ).value
                                )
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            isDragging = true
                                            startTransfer(
                                                transferData = DragAndDropTransferData(
                                                    clipData = ClipData.newPlainText(
                                                        "text",
                                                        ""
                                                    )
                                                )
                                            )
                                            isDragging = false
                                        }
                                    )
                                }
                        )
                    }

                    Box(
                        modifier = Modifier.padding(start = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "TOTAL SCORE",
                                    color = Color.Yellow,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "$totalScore",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    style = androidx.compose.ui.text.TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            blurRadius = 8f,
                                            offset = Offset(2f, 2f)
                                        )
                                    )
                                )
                            }
                        }

                    }
                }
            }
        }

    // Add this at the top of your @Composable function, before the Box


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.60f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.grass_03),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            platforms.forEach { platform ->
                drawRect(
                    color = Color(0xFF5B9BD5),
                    topLeft = Offset(platform.x, platform.y),
                    size = Size(platform.width, platform.height)
                )
            }


            val imageWidth = 180f
            val imageHeight = 180f


            // Updated coin drawing code
            coins.forEach { coin ->
                if (!coin.collected && coinBitmap != null) {
                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawBitmap(
                            coinBitmap,
                            coin.x - imageWidth / 2,
                            coin.y - imageHeight / 2,
                            null
                        )
                    }
                }
            }


            // Draw the player (circle for representation)
            drawCircle(
                color = Color(0xFF666666),
                radius = 50f,
                center = Offset(playerX, playerY)  // Use the current player position
            )

        }

    }
}
}


