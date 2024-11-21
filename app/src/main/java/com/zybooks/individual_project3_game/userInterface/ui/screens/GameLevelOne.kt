@file:OptIn(ExperimentalFoundationApi::class)

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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.delay
import kotlin.math.abs


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
fun MazeGame() {
    var playerX by remember { mutableStateOf(50f) }
    var playerY by remember { mutableStateOf(250f) }
    var direction by remember { mutableStateOf("none") }
    var dragBoxIndex by remember { mutableStateOf(0) }
    var canvasWidth by remember { mutableStateOf(0f) }

    val scale = 4f

    val platforms = remember {
        mutableStateListOf(
            Platform(0f * scale, 120f * scale, 100f * scale, 20f * scale),        // Starting platform
            Platform(100f * scale, 100f * scale, 20f * scale, 40f * scale),       // First vertical up
            Platform(120f * scale, 100f * scale, 100f * scale, 20f * scale),      // First bridge
            Platform(220f * scale, 100f * scale, 20f * scale, 60f * scale),       // Second vertical down
            Platform(240f * scale, 140f * scale, 100f * scale, 20f * scale),      // Second bridge
            Platform(340f * scale, 120f * scale, 20f * scale, 40f * scale),       // Third vertical up
            Platform(360f * scale, 120f * scale, 100f * scale, 20f * scale),      // Third bridge
            Platform(460f * scale, 120f * scale, 20f * scale, 60f * scale),       // Fourth vertical down
            Platform(480f * scale, 160f * scale, 100f * scale, 20f * scale),      // Fourth bridge
            Platform(580f * scale, 140f * scale, 20f * scale, 40f * scale),       // Fifth vertical up
            Platform(600f * scale, 140f * scale, 100f * scale, 20f * scale),      // Fifth bridge
            Platform(700f * scale, 140f * scale, 20f * scale, 60f * scale),       // Sixth vertical down
            Platform(720f * scale, 180f * scale, 100f * scale, 20f * scale),      // Final platform
        )
    }

    val coins = remember {
        mutableStateListOf(
            Coin(50f * scale, 120f * scale),      // Middle of starting platform
            Coin(170f * scale, 100f * scale),     // Middle of first bridge
            Coin(290f * scale, 140f * scale),     // Middle of second bridge
            Coin(410f * scale, 120f * scale),     // Middle of third bridge
            Coin(530f * scale, 160f * scale),     // Middle of fourth bridge
            Coin(650f * scale, 140f * scale),     // Middle of fifth bridge
            Coin(770f * scale, 180f * scale)      // Middle of final platform
        )
    }
    var score by remember { mutableStateOf(0) }

    fun isOnPlatform(x: Float, y: Float, platforms: List<Platform>): Boolean {
        platforms.forEach { platform ->
            if (x >= platform.x &&
                x <= platform.x + platform.width &&
                y >= platform.y &&
                y <= platform.y + platform.height
            ) {
                return true
            }
        }
        return false
    }

    LaunchedEffect(direction) {
        when(direction) {
            "up" -> {
                repeat(50) {
                    delay(16)
                    val newY = playerY - 2f  // Reduced movement speed
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                    }
                }
            }
            "down" -> {
                repeat(50) {
                    delay(16)
                    val newY = playerY + 2f  // Reduced movement speed
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                    }
                }
            }
            "left" -> {
                repeat(50) {
                    delay(16)
                    val newX = playerX - 2f  // Reduced movement speed
                    if (isOnPlatform(newX, playerY, platforms)) {
                        playerX = newX
                    }
                }
            }
            "right" -> {
                repeat(50) {
                    delay(16)
                    val newX = playerX + 2f  // Reduced movement speed
                    if (isOnPlatform(newX, playerY, platforms)) {
                        playerX = newX
                    }
                }
            }
        }
        direction = "none"

        coins.forEach { coin ->
            if (!coin.collected &&
                abs(playerX - coin.x) < 15f &&  // Reduced collision radius
                abs(playerY - coin.y) < 15f
            ) {
                coin.collected = true
                score += 1
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)  // Ensure no padding interferes with the split
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)  // Changed from 0.2f to 0.25f for 1/4 of the screen
                .padding(4.dp)  // Reduced padding to prevent overflow
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)  // Reduced padding from 10.dp
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = index
                                        direction = when(index) {
                                            0 -> "up"
                                            1 -> "down"
                                            2 -> "left"
                                            3 -> "right"
                                            else -> "none"
                                        }
                                        return true
                                    }
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when(index) {
                            0 -> "Up"
                            1 -> "Down"
                            2 -> "Left"
                            3 -> "Right"
                            else -> ""
                        },
                        color = Color.Gray,
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    this@Row.AnimatedVisibility(
                        visible = index == dragBoxIndex,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Arrow",
                            tint = Color.Red,
                            modifier = Modifier
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            startTransfer(
                                                transferData = DragAndDropTransferData(
                                                    clipData = ClipData.newPlainText(
                                                        "text",
                                                        ""
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.60f)
                .background(Color.White)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)  // Ensure no padding interferes with the canvas
            ) {
                // Draw platforms
                platforms.forEach { platform ->
                    drawRect(
                        color = Color(0xFF5B9BD5),
                        topLeft = Offset(platform.x, platform.y),
                        size = Size(platform.width, platform.height)
                    )
                }

                // Draw uncollected coins
                coins.forEach { coin ->
                    if (!coin.collected) {
                        drawCircle(
                            color = Color(0xFFFFD700),
                            radius = 30f,  // Reduced coin size
                            center = Offset(coin.x, coin.y)
                        )
                    }
                }

                // Draw player
                drawCircle(
                    color = Color(0xFF666666),
                    radius = 30f,  // Reduced player size
                    center = Offset(100f, -100f)
                )
            }
        }
    }
}