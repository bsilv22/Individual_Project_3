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
    var playerX by remember { mutableStateOf(20f * 6f) }
    var playerY by remember { mutableStateOf(130f * 6f) }
    var direction by remember { mutableStateOf("none") }
    var dragBoxIndex by remember { mutableStateOf(0) }
    var canvasWidth by remember { mutableStateOf(0f) }
    val scale = 6f

    val platforms = remember {
        mutableStateListOf(
            Platform(0f * scale, 120f * scale, 100f * scale, 20f * scale),
            Platform(100f * scale, 100f * scale, 20f * scale, 40f * scale),
            Platform(120f * scale, 100f * scale, 100f * scale, 20f * scale),
            Platform(220f * scale, 100f * scale, 20f * scale, 60f * scale),
            Platform(240f * scale, 140f * scale, 100f * scale, 20f * scale),
            Platform(340f * scale, 120f * scale, 20f * scale, 40f * scale),
            Platform(360f * scale, 120f * scale, 100f * scale, 20f * scale),
            Platform(460f * scale, 120f * scale, 20f * scale, 60f * scale),
            Platform(480f * scale, 160f * scale, 100f * scale, 20f * scale),
            Platform(580f * scale, 140f * scale, 20f * scale, 40f * scale),
            Platform(600f * scale, 140f * scale, 100f * scale, 20f * scale),
            Platform(700f * scale, 140f * scale, 20f * scale, 60f * scale),
            Platform(720f * scale, 180f * scale, 100f * scale, 20f * scale),
        )
    }

    val coins = remember {
        mutableStateListOf(
            Coin(50f * scale, 130f * scale),
            Coin(170f * scale, 110f * scale),
            Coin(290f * scale, 150f * scale),
            Coin(410f * scale, 130f * scale),
            Coin(530f * scale, 170f * scale),
            Coin(650f * scale, 150f * scale),
            Coin(770f * scale, 190f * scale)
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
                    val newY = playerY - 2f
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                    }
                }
            }
            "down" -> {
                repeat(50) {
                    delay(16)
                    val newY = playerY + 2f
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                    }
                }
            }
            "right" -> {
                repeat(50) {
                    delay(16)
                    val newX = playerX + 2f
                    if (isOnPlatform(newX, playerY, platforms)) {
                        playerX = newX
                    }
                }
            }
        }
        direction = "none"

        coins.forEach { coin ->
            if (!coin.collected &&
                abs(playerX - coin.x) < 15f &&
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
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
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
                                            direction = "up"
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

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(150.dp)
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

                coins.forEach { coin ->
                    if (!coin.collected) {
                        drawCircle(
                            color = Color(0xFFFFD700),
                            radius = 30f,
                            center = Offset(coin.x, coin.y)
                        )
                    }
                }

                drawCircle(
                    color = Color(0xFF666666),
                    radius = 30f,
                    center = Offset(playerX, playerY)
                )
            }
        }
    }
}