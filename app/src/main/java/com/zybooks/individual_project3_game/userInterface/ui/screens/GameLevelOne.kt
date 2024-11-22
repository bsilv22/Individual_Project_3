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
    var playerX by remember { mutableStateOf(50f) }
    var playerY by remember { mutableStateOf(250f) }
    var direction by remember { mutableStateOf("none") }
    var dragBoxIndex by remember { mutableStateOf(0) }
    var canvasWidth by remember { mutableStateOf(0f) }

    val scale =6f

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
            Coin(50f * scale, 130f * scale),      // Center of starting platform (y: 120 + 20/2)
            Coin(170f * scale, 110f * scale),     // Center of first bridge (y: 100 + 20/2)
            Coin(290f * scale, 150f * scale),     // Center of second bridge (y: 140 + 20/2)
            Coin(410f * scale, 130f * scale),     // Center of third bridge (y: 120 + 20/2)
            Coin(530f * scale, 170f * scale),     // Center of fourth bridge (y: 160 + 20/2)
            Coin(650f * scale, 150f * scale),     // Center of fifth bridge (y: 140 + 20/2)
            Coin(770f * scale, 190f * scale)      // Center of final platform (y: 180 + 20/2)
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
            .padding(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
                .padding(4.dp)
        ) {
            // Left half containing the direction boxes
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                // Up box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = 0
                                        direction = "up"
                                        return true
                                    }
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Up",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    this@Row.AnimatedVisibility(
                        visible = dragBoxIndex == 0,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {

                    }
                }


                // Down box
                // Down box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = 1
                                        direction = "down"
                                        return true
                                    }
                                }
                            }
                        )
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Down",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                        Canvas(modifier = Modifier.size(24.dp)) {
                            val width = size.width
                            val height = size.height

                            // Draw arrow
                            drawPath(
                                path = androidx.compose.ui.graphics.Path().apply {
                                    // Start at top center
                                    moveTo(width / 2, 0f)
                                    // Draw line to bottom right
                                    lineTo(width, height / 2)
                                    // Draw line to bottom left
                                    lineTo(width / 2, height)
                                    // Draw line to top left
                                    lineTo(0f, height / 2)
                                    // Close the path
                                    close()
                                },
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Right box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = 2
                                        direction = "right"
                                        return true
                                    }
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Right",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )


                }
            }

            // Empty space for right half
            // Replace the Spacer with this Box
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment.Center
            )
                 {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(150.dp)  // Added explicit size for better visibility
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.60f)
        ) {
            // Add the background image
            Image(
                painter = painterResource(id = R.drawable.grass_03),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Add your canvas on top
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
                    center = Offset(
                        x = (20f) * scale,  // Move in from left edge by radius
                        y = (130f) * scale  // Platform y (120) + half platform height (20/2) for vertical center
                    )
                )
            }
        }}}